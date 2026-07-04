package com.example.expensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.room.model.ExpenseTemplate
import com.example.expensetracker.room.repository.ExpenseRepository
import com.example.expensetracker.room.repository.ExpenseTemplateRepository
import com.example.expensetracker.room.model.Expense
import com.example.expensetracker.utils.TimeUtils
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExpenseTemplateViewModel(
    private val expenseTemplateRepository: ExpenseTemplateRepository,
    private val expenseRepository: ExpenseRepository
): ViewModel() {
    val templates: StateFlow<List<ExpenseTemplate>> = expenseTemplateRepository.allTemplate
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun addTemplate(title: String, amount: Double, categoryId: Int?) {
        viewModelScope.launch {
            expenseTemplateRepository.insert(
                ExpenseTemplate(
                    title = title,
                    amount = amount,
                    categoryId = categoryId
                )
            )
        }
    }

    fun addTemplate(template: ExpenseTemplate) {
        viewModelScope.launch {
            expenseTemplateRepository.insert(template)
        }
    }

    fun deleteTemplate(template: ExpenseTemplate) {
        viewModelScope.launch {
            expenseTemplateRepository.delete(template)
        }
    }

    fun updateTemplate(template: ExpenseTemplate) {
        viewModelScope.launch {
            expenseTemplateRepository.update(template)
        }
    }

    fun setTemplateEnabled(template: ExpenseTemplate, enabled: Boolean) {
        viewModelScope.launch {
            val updatedTemplate = template.copy(enabled = enabled)
            expenseTemplateRepository.update(updatedTemplate)
        }
    }

    fun addTemplateExpenseToday(template: ExpenseTemplate) {
        viewModelScope.launch {
            addExpenseFromTemplate(template, TimeUtils.todayStartMillis())
        }
    }

    fun addDueTemplateExpenses() {
        viewModelScope.launch {
            val today = TimeUtils.todayStartMillis()
            expenseTemplateRepository.getEnabledTemplates()
                .filter { it.lastAddedDay != today }
                .forEach { addExpenseFromTemplate(it, today) }
        }
    }

    private suspend fun addExpenseFromTemplate(template: ExpenseTemplate, today: Long) {
        expenseRepository.insert(
            Expense(
                title = template.title,
                amount = template.amount,
                date = System.currentTimeMillis(),
                categoryId = template.categoryId
            )
        )
        expenseTemplateRepository.update(template.copy(lastAddedDay = today))
    }
}

class ExpenseTemplateViewModelFactory(
    private val expenseTemplateRepository: ExpenseTemplateRepository,
    private val expenseRepository: ExpenseRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ExpenseTemplateViewModel(expenseTemplateRepository, expenseRepository) as T
    }

}
