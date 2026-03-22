package com.example.expensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.room.model.Expense
import com.example.expensetracker.room.repository.ExpenseRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExpenseViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    val allExpenses: StateFlow<List<Expense>> = repository.allExpenses
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val totalAmount: StateFlow<Double> = repository.totalAmount
        .map { it ?: 0.0 }
        .stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0.0
        )

    fun getExpense(id: Int): StateFlow<Expense?> {
        return repository
            .getExpenseId(id)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                null
            )
    }

    fun addExpense(title: String, amount: Double) {
        viewModelScope.launch {

            repository.insert(
                Expense(
                    title = title,
                    amount = amount,
                    date = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.delete(expense)
        }
    }
}

class ExpenseViewModelFactory(private val repository: ExpenseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ExpenseViewModel(repository) as T
    }
}
