package com.example.expensetracker.room.repository

import com.example.expensetracker.room.dao.SettingDao
import com.example.expensetracker.room.model.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val dao: SettingDao) {
    val settings: Flow<Settings> = dao.getSettings().map { it ?: Settings() }

    suspend fun upsertSettings(settings: Settings) = dao.upsertSettings(settings)
}
