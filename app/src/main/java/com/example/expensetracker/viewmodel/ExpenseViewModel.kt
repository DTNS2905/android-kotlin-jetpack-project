package com.example.expensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Update
import com.example.expensetracker.constants.TimeFilter
import com.example.expensetracker.room.model.Expense
import com.example.expensetracker.room.repository.ExpenseRepository
import com.example.expensetracker.ui.components.MessageType
import com.example.expensetracker.utils.toTimeRange
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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

data class Fillters (
    val time: TimeFilter
)

class ExpenseViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _selectedId = MutableStateFlow<Int?>(null)

    private val _uiEvent = MutableSharedFlow<UiEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val _selectedFilters = MutableStateFlow(Fillters(TimeFilter.ALL))

    val uiEvent = _uiEvent.asSharedFlow()

    val selectedFilter = _selectedFilters.asStateFlow()

    fun loadExpense(id: Int) {
        _selectedId.value = id
    }

    fun setFilter( update: (Fillters) -> Fillters) {
        _selectedFilters.update {  update(it) }
    }

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

    val getAllExpenses: StateFlow<List<Expense>> = repository.allExpenses
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    @OptIn(ExperimentalCoroutinesApi::class)
    val getAllFilteredExpenses: StateFlow<List<Expense>> =
        selectedFilter.flatMapLatest { filter ->
            val (from, to) = filter.time.toTimeRange()
            repository.getExpenseFrom(from, to)
        }. stateIn(
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

    fun addExpense(title: String, amount: Double, categoryId: Int) {
        viewModelScope.launch {

            repository.insert(
                Expense(
                    title = title,
                    amount = amount,
                    date = System.currentTimeMillis(),
                    categoryId = categoryId
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
