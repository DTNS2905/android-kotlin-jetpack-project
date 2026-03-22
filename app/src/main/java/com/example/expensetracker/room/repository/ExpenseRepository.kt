package com.example.expensetracker.room.repository

import com.example.expensetracker.room.dao.ExpenseDao
import com.example.expensetracker.room.model.Expense
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val dao: ExpenseDao) {
    val allExpenses: Flow<List<Expense>> = dao.getAllExpenses()

    val totalAmount: Flow<Double?> = dao.getTotalAmount()

    fun  getExpenseId(id: Int) = dao.getExpenseById(id)

    suspend fun insert(expense: Expense) = dao.insertExpense(expense)

    suspend fun delete(expense: Expense) = dao.deleteExpense(expense)
}
