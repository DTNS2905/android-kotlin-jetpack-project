package com.example.expensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.room.model.Expense
import com.example.expensetracker.room.repository.ExpenseRepository
import com.example.expensetracker.ui.components.MessageType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ExpenseDetailState {
    object Loading : ExpenseDetailState()
    data class Success(
        val expense: Expense
    ) : ExpenseDetailState()
    object NotFound : ExpenseDetailState()
}

sealed class UiEvent {
    data class ShowMessage(val message: String, val type: MessageType) : UiEvent()
}

class ExpenseViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _selectedId = MutableStateFlow<Int?>(null)

    private val _uiEvent = MutableSharedFlow<UiEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val uiEvent = _uiEvent.asSharedFlow()


    @OptIn(ExperimentalCoroutinesApi::class)
    val expenseDetailState: StateFlow<ExpenseDetailState> = _selectedId
            .filterNotNull()
            .flatMapLatest { id ->
            repository.getExpenseId(id).map { expense ->
                if (expense != null) ExpenseDetailState.Success(expense)
                else ExpenseDetailState.NotFound
            }
        }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                ExpenseDetailState.Loading
            )

    fun loadExpense(id: Int) {
        _selectedId.value = id
    }

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

    fun addExpense(title: String, amount: Double) {
        viewModelScope.launch {

            repository.insert(
                Expense(
                    title = title,
                    amount = amount,
                    date = System.currentTimeMillis()
                )
            )
            _uiEvent.emit(
                UiEvent
                    .ShowMessage(
                        "Expense added successfully",
                        MessageType.SUCCESS
                )
            )
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.delete(expense)
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            repository.update(expense)
        }
    }
}

class ExpenseViewModelFactory(private val repository: ExpenseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ExpenseViewModel(repository) as T
    }
}
