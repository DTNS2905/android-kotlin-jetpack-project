package com.example.expensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.constants.TimeFilter
import com.example.expensetracker.room.model.MonthlyBudget
import com.example.expensetracker.room.model.Settings
import com.example.expensetracker.room.repository.ExpenseRepository
import com.example.expensetracker.room.repository.MonthlyBudgetRepository
import com.example.expensetracker.room.repository.SettingsRepository
import com.example.expensetracker.utils.toTimeRange
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

data class BudgetState(
    val budget: Double = 0.0,
    val spent: Double = 0.0,
    val alertThreshold: Int = 80
) {
    val progress: Float get() = if(budget <= 0) 0f else (spent / budget).toFloat()
    val isOverBudget: Boolean get() =budget > 0 && spent > budget
    val isNearLimit: Boolean get() = budget > 0 && progress >= alertThreshold / 100f
}


class SettingViewModel (
    private val expenseRepository: ExpenseRepository,
    private val settingsRepository: SettingsRepository,
    private val monthlyBudgetRepository: MonthlyBudgetRepository
): ViewModel() {


    private val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    private val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

    init {
        viewModelScope.launch {
            val current = monthlyBudgetRepository.getBudgetForMonth(currentYear, currentMonth).first()
            if(current == null) {
                val previousBudget = monthlyBudgetRepository.getPreviousBudget(currentYear, currentMonth)
                previousBudget?.let { budget ->
                    monthlyBudgetRepository.upsert(MonthlyBudget(currentYear, currentMonth, budget.amount))
                }
            }

        }
    }

    val budgetState: StateFlow<BudgetState> = combine(
        expenseRepository.getTotalAmountForPeriod(
            from = TimeFilter.THIS_MONTH.toTimeRange().first,
            to = TimeFilter.THIS_MONTH.toTimeRange().second
        ),
        settingsRepository.settings,
        monthlyBudgetRepository.getBudgetForMonth(
            year = currentYear,
            month = currentMonth
        ),
    ) { spent, settings, monthlyBudget ->
        BudgetState(
            budget = monthlyBudget?.amount ?: 0.0,
            spent = spent ?: 0.0,
            alertThreshold = settings.budgetAlert
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        BudgetState()
    )

    val currencySymbol: StateFlow<String> = settingsRepository.settings
        .map { it.currencySymbol }
        .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "$"
    )

    val username: StateFlow<String> = settingsRepository.settings
        .map { it.name }
        .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "User"
    )

    val imagePath: StateFlow<String?> = settingsRepository.settings
        .map { it.imagePath }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    val darkMode: StateFlow<Boolean> = settingsRepository.settings
        .map { it.darkMode }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val dailyReminders: StateFlow<Boolean> = settingsRepository.settings
        .map { it.dailyReminders }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isLoaded: StateFlow<Boolean> = settingsRepository.settings
        .map { true }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun updateSettings(
        alertThreshold: Int = budgetState.value.alertThreshold,
        currencySymbol: String = this.currencySymbol.value,
        username: String = this.username.value,
        imagePath: String? = this.imagePath.value,
        darkMode: Boolean = this.darkMode.value,
        dailyReminders: Boolean = this.dailyReminders.value
    ) {
        viewModelScope.launch {
            settingsRepository.upsertSettings(
                Settings(
                    budgetAlert = alertThreshold,
                    currencySymbol = currencySymbol,
                    name = username,
                    imagePath = imagePath,
                    darkMode = darkMode,
                    dailyReminders = dailyReminders
                )
            )
        }
    }

    fun setBudgetForMonth(year: Int, month: Int, amount: Double) {
        viewModelScope.launch {
            monthlyBudgetRepository.upsert(MonthlyBudget(year, month, amount))
        }
    }

}

class BudgetViewModelFactory(
    private val expenseRepository: ExpenseRepository,
    private val settingsRepository: SettingsRepository,
    private val monthlyBudgetRepository: MonthlyBudgetRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SettingViewModel(expenseRepository, settingsRepository, monthlyBudgetRepository ) as T
    }
}
