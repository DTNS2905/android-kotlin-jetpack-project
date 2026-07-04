package com.example.expensetracker.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val color: Long,
    val icon: String = "receipt"
)