package com.example.expensetracker.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.expensetracker.room.model.MonthlyBudget
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthlyBudgetDao {
    @Query("SELECT * FROM monthly_budget WHERE year = :year AND month = :month LIMIT 1")
    fun getBudgetForMonth(year: Int, month: Int): Flow<MonthlyBudget?>

    @Upsert
    suspend fun upsert(budget: MonthlyBudget)

    @Query("""
    SELECT * FROM monthly_budget 
    WHERE (year < :year) OR (year = :year AND month < :month) 
    ORDER BY year DESC, month DESC 
    LIMIT 1
""")
    suspend fun getPreviousBudget(year: Int, month: Int): MonthlyBudget?
}