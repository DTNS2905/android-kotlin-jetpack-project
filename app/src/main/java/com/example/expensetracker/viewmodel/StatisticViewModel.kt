package com.example.expensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.room.model.Category
import com.example.expensetracker.room.repository.CategoryRepository
import com.example.expensetracker.room.repository.ExpenseRepository
import com.example.expensetracker.room.repository.SettingsRepository
import com.example.expensetracker.utils.daysInMonth
import com.example.expensetracker.utils.next
import com.example.expensetracker.utils.previous
import com.example.expensetracker.utils.toRange
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

data class YearMonth(
    val year: Int,
    val month: Int
)

data class CategoryStat(
    val category: Category?,
    val amount: Double,
    val percentage: Double,
    val count: Int = 0
)

data class DailyStat(
    val day: Int,
    val amount: Double
)

data class StatisticState(
    val totalSpent: Double = 0.0,
    val transactionCount: Int = 0,
    val avgPerDay: Double = 0.0,
    val categoryStats: List<CategoryStat> = emptyList(),
    val dailyStats: List<DailyStat> = emptyList(),
    val currencySymbol: String = "$"
)

class StatisticViewModel(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
    private val settingsRepository: SettingsRepository
): ViewModel() {
    private val now = Calendar.getInstance()
    private val _selectedMonth =
        MutableStateFlow(YearMonth(
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH) + 1)
        )

    val selectedMonth: StateFlow<YearMonth> = _selectedMonth.asStateFlow()

    fun previousMonth() {
        _selectedMonth.value = _selectedMonth.value.previous()
    }

    fun nextMonth() {
        _selectedMonth.value = _selectedMonth.value.next()
    }

    fun selectMonth(ym: YearMonth) {
        _selectedMonth.value = ym
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val expensesInMonth = _selectedMonth.flatMapLatest { yearMonth ->
        val (from, to) = yearMonth.toRange()
        expenseRepository.getExpenses(from, to, null)
    }

    val statisticState: StateFlow<StatisticState> = combine(
        expensesInMonth,
        categoryRepository.allCategories,
        settingsRepository.settings
    ) { expenses, categories, settings ->
        val categoryMap = categories.associateBy { it.id }
        val total = expenses.sumOf { it.amount }
        val daysInMonth = _selectedMonth.value.daysInMonth()
        val cal = Calendar.getInstance()

        val categoryStats = expenses.groupBy { it.categoryId  }
            .map { (categoryId, group) ->
                val category = categoryMap[categoryId]
                val amount = group.sumOf { it.amount }
                val percentage = if (total > 0) (amount / total) else 0.0
                CategoryStat(category, amount, percentage, count = group.size)
            }
            .sortedByDescending { it.amount }

        val dailyMap = expenses.groupBy { expense ->
            cal.timeInMillis = expense.date
            cal.get(Calendar.DAY_OF_MONTH)
        }

        val dailyStats = (1..daysInMonth).map { day ->
            DailyStat(day, dailyMap[day]?.sumOf { it.amount } ?: 0.0)
        }

        StatisticState(
            totalSpent = total,
            transactionCount = expenses.size,
            avgPerDay = if (daysInMonth > 0) total / daysInMonth else 0.0,
            categoryStats = categoryStats,
            dailyStats = dailyStats,
            currencySymbol = settings.currencySymbol
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatisticState())

}

class StatisticViewModelFactory(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return StatisticViewModel(expenseRepository, categoryRepository, settingsRepository) as T
    }
}