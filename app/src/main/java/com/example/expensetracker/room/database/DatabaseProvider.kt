package com.example.expensetracker.room.database

import androidx.room.Room
import android.content.Context

object DatabaseProvider {
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "expense_db"
                    )
                    .addMigrations(AppDatabase.MIGRATION_3_4)
                    .fallbackToDestructiveMigration(false).build()
            INSTANCE = instance
            instance
        }
    }
}