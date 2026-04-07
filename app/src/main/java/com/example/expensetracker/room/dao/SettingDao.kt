package com.example.expensetracker.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.expensetracker.room.model.Settings
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDao {
    @Query("SELECT * FROM settings")
    fun getSettings(): Flow<Settings?>

    @Upsert
    suspend fun upsertSettings(settings: Settings)

}