package com.example.expensetracker.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.expensetracker.room.model.ExpenseTemplate
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseTemplateDao {
    @Query("SELECT * FROM expense_templates ORDER BY title ASC")
    fun getAllTemplates(): Flow<List<ExpenseTemplate>>

    @Query("SELECT * FROM expense_templates WHERE enabled = 1")
    suspend fun getEnabledTemplates(): List<ExpenseTemplate>

    @Insert
    suspend fun insertTemplate(template: ExpenseTemplate)

    @Update
    suspend fun updateTemplate(template: ExpenseTemplate)

    @Delete
    suspend fun deleteTemplate(template: ExpenseTemplate)
}
