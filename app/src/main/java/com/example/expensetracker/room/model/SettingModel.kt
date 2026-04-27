package com.example.expensetracker.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "settings",
)
data class Settings(
    @PrimaryKey val id: Int = 1,
    val monthlyBudget: Double = 0.0,
    val budgetAlert: Int = 80,
    val currencySymbol: String = "$",
    val name: String = "User",
    val imagePath: String? = null,
)