package com.example.expensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.room.model.Settings
import com.example.expensetracker.room.repository.ExpenseRepository
import com.example.expensetracker.room.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BugetState(
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

    val budgetState: StateFlow<BugetState> = combine(
        expenseRepository.totalAmount,
        settingsRepository.settings
    ) { spent, settings ->
        BugetState(
            budget = settings.monthlyBudget,
            spent = spent ?: 0.0,
            alertThreshold = settings.budgetAlert

        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        BugetState()
    )

    fun updateSettings(
        budget: Double = budgetState.value.budget,
        alertThreshold: Int = budgetState.value.alertThreshold
    ) {
        viewModelScope.launch {
            settingsRepository.upsertSettings(
                Settings(monthlyBudget = budget, budgetAlert = alertThreshold)
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
