package com.example.expensetracker.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.expensetracker.room.dao.CategoryDao
import com.example.expensetracker.room.dao.ExpenseDao
import com.example.expensetracker.room.dao.ExpenseTemplateDao
import com.example.expensetracker.room.dao.MonthlyBudgetDao
import com.example.expensetracker.room.dao.SettingDao
import com.example.expensetracker.room.model.Category
import com.example.expensetracker.room.model.Expense
import com.example.expensetracker.room.model.MonthlyBudget
import com.example.expensetracker.room.model.Settings
import com.example.expensetracker.room.model.ExpenseTemplate

@Database(
    entities = [
        Expense::class,
        Category::class,
        Settings::class,
        MonthlyBudget::class,
        ExpenseTemplate::class
    ],
    version = 9
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao

    abstract fun settingDao(): SettingDao

    abstract fun monthlyBudgetDao(): MonthlyBudgetDao

    abstract fun expenseTemplateDao(): ExpenseTemplateDao
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

        val MIGRATION_5_6: Migration = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE expenses ADD COLUMN notes TEXT")
                db.execSQL("ALTER TABLE settings ADD COLUMN darkMode INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE settings ADD COLUMN dailyReminders INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_6_7: Migration = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE categories ADD COLUMN icon TEXT NOT NULL DEFAULT 'receipt'")
            }
        }

        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS monthly_budget (
                year INTEGER NOT NULL,
                month INTEGER NOT NULL,
                amount REAL NOT NULL,
                PRIMARY KEY(year, month)
            )
        """
                )
            }
        }

        val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
            CREATE TABLE IF NOT EXISTS expense_templates (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                title TEXT NOT NULL,
                amount REAL NOT NULL,
                categoryId INTEGER,
                enabled INTEGER NOT NULL DEFAULT 1,
                lastAddedDay INTEGER,
                FOREIGN KEY(categoryId) REFERENCES categories(id) ON DELETE SET NULL
            )
        """)
            }
        }
    }
}
