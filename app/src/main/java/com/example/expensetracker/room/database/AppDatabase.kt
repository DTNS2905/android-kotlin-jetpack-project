package com.example.expensetracker.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.expensetracker.room.dao.CategoryDao
import com.example.expensetracker.room.dao.ExpenseDao
import com.example.expensetracker.room.model.Category
import com.example.expensetracker.room.model.Expense

@Database(
    entities = [Expense::class, Category::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao
}
