package com.example.expensetracker.room.repository

import com.example.expensetracker.room.dao.MonthlyBudgetDao
import com.example.expensetracker.room.model.MonthlyBudget

class MonthlyBudgetRepository(private val dao: MonthlyBudgetDao) {
    fun getBudgetForMonth(year: Int, month: Int) = dao.getBudgetForMonth(year, month)
    suspend fun upsert(budget:MonthlyBudget) = dao.upsert(budget)

    suspend fun getPreviousBudget(year: Int, month: Int) = dao.getPreviousBudget(year, month)
}
