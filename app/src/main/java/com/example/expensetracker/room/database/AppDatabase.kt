package com.example.expensetracker.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.expensetracker.room.dao.ExpenseDao
import com.example.expensetracker.room.model.Expense

@Database(
    entities = [Expense::class],
    version =1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}