package com.example.expensetracker.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.expensetracker.room.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE id = :id")
    fun getExpenseById(id: Int): Flow<Expense?>

    @Query("SELECT SUM(amount) FROM expenses")
    fun getTotalAmount(): Flow<Double?>

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE (:categoryId IS NULL OR categoryId = :categoryId) ORDER BY date DESC")
    fun getAllExpenses(categoryId: Int?): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date >= :from AND date <= :to AND (:categoryId IS NULL OR categoryId = :categoryId) ORDER BY date DESC")
    fun getExpenseFrom(from: Long, to: Long, categoryId: Int?): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE title LIKE '%' || :query || '%' AND (:categoryId IS NULL OR categoryId = :categoryId) AND date >= :from AND date <= :to ORDER BY date DESC")
    fun searchExpense(query: String, from: Long, to: Long, categoryId: Int?): Flow<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses WHERE date >= :from AND date <= :to")
    fun getTotalAmountForPeriod(from: Long, to: Long): Flow<Double?>
}