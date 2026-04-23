package com.example.expensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.constants.TimeFilter
import com.example.expensetracker.room.model.Settings
import com.example.expensetracker.room.repository.ExpenseRepository
import com.example.expensetracker.room.repository.SettingsRepository
import com.example.expensetracker.utils.toTimeRange
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BudgetState(
    val budget: Double = 0.0,
    val spent: Double = 0.0,
    val alertThreshold: Int = 80
) {
    val progress: Float get() = if(budget <= 0) 0f else (spent / budget).toFloat()
    val isOverBudget: Boolean get() =budget > 0 && spent > budget
    val isNearLimit: Boolean get() = budget > 0 && progress >= alertThreshold / 100f
}


class BudgetViewModel (
    private val expenseRepository: ExpenseRepository,
    private val settingsRepository: SettingsRepository
): ViewModel() {

    val budgetState: StateFlow<BudgetState> = combine(
        expenseRepository.getTotalAmountForPeriod(
            from = TimeFilter.THIS_MONTH.toTimeRange().first,
            to = TimeFilter.THIS_MONTH.toTimeRange().second
        ),
        settingsRepository.settings
    ) { spent, settings ->
        BudgetState(
            budget = settings.monthlyBudget,
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

    fun updateSettings(
        budget: Double = budgetState.value.budget,
        alertThreshold: Int = budgetState.value.alertThreshold,
        currencySymbol: String = this.currencySymbol.value
    ) {
        viewModelScope.launch {
            settingsRepository.upsertSettings(
                Settings(monthlyBudget = budget, budgetAlert = alertThreshold, currencySymbol = currencySymbol)
            )
        }
    }

}

class BudgetViewModelFactory(
    private val expenseRepository: ExpenseRepository,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return BudgetViewModel(expenseRepository, settingsRepository ) as T
    }
}
