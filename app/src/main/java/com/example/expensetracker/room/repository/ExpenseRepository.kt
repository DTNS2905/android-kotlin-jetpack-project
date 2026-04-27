package com.example.expensetracker.room.repository

import com.example.expensetracker.room.dao.ExpenseDao
import com.example.expensetracker.room.model.Expense
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val dao: ExpenseDao) {
    val allExpenses: Flow<List<Expense>> = dao.getAllExpenses()

    val totalAmount: Flow<Double?> = dao.getTotalAmount()

    fun  getExpenseId(id: Int) = dao.getExpenseById(id)

    suspend fun insert(expense: Expense) = dao.insertExpense(expense)

    suspend fun update(expense: Expense) = dao.updateExpense(expense)

    suspend fun delete(expense: Expense) = dao.deleteExpense(expense)

    suspend fun deleteAllExpense() = dao.deleteAllExpenses()

    fun getAllExpenses(categoryId: Int?) = dao.getAllExpenses(categoryId)

    fun getExpenses(from: Long, to: Long, categoryId: Int?) =
        dao.getExpenses(from, to, categoryId)

    fun searchExpense(query: String, from: Long, to: Long, categoryId: Int?) =
        dao.searchExpense(query, from, to, categoryId)

    fun getTotalAmountForPeriod(from: Long, to: Long): Flow<Double?> =
        dao.getTotalAmountForPeriod(from, to)
}
