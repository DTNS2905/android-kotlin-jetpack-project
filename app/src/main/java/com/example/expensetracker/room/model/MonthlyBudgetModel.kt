package com.example.expensetracker.room.model

import androidx.room.Entity

@Entity(tableName = "monthly_budget", primaryKeys = ["year", "month"])
data class MonthlyBudget(
    val year: Int,
    val month: Int,
    val amount: Double
)