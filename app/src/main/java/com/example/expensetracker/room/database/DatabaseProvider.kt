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
                    .addMigrations(AppDatabase.MIGRATION_4_5)
                    .addMigrations(AppDatabase.MIGRATION_5_6)
                    .addMigrations(AppDatabase.MIGRATION_6_7)
                    .addMigrations(AppDatabase.MIGRATION_7_8)
                    .addMigrations(AppDatabase.MIGRATION_8_9)
                    .fallbackToDestructiveMigration(false).build()
            INSTANCE = instance
            instance
        }
    }
}
