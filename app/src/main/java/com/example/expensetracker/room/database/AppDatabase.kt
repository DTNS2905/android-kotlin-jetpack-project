package com.example.expensetracker.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.expensetracker.room.dao.CategoryDao
import com.example.expensetracker.room.dao.ExpenseDao
import com.example.expensetracker.room.dao.SettingDao
import com.example.expensetracker.room.model.Category
import com.example.expensetracker.room.model.Expense
import com.example.expensetracker.room.model.Settings

@Database(
    entities = [Expense::class, Category::class, Settings::class],
    version = 5
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao

    abstract fun settingDao(): SettingDao

    companion object {
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE settings ADD COLUMN currencySymbol TEXT NOT NULL DEFAULT '$'")
            }
        }

        val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE settings ADD COLUMN name TEXT NOT NULL DEFAULT 'User'")
                db.execSQL("ALTER TABLE settings ADD COLUMN imagePath TEXT")
            }
        }
    }
}
