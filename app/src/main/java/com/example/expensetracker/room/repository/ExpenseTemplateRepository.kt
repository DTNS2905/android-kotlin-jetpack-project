package com.example.expensetracker.room.repository

import com.example.expensetracker.room.dao.ExpenseTemplateDao
import com.example.expensetracker.room.model.ExpenseTemplate
import kotlinx.coroutines.flow.Flow

class ExpenseTemplateRepository(
    private val dao: ExpenseTemplateDao
) {
    val allTemplate: Flow<List<ExpenseTemplate>> = dao.getAllTemplates()

    suspend fun insert(template: ExpenseTemplate) = dao.insertTemplate(template)

    suspend fun update(template: ExpenseTemplate) = dao.updateTemplate(template)

    suspend fun delete(template: ExpenseTemplate) = dao.deleteTemplate(template)

    suspend fun getEnabledTemplates(): List<ExpenseTemplate> = dao.getEnabledTemplates()

}