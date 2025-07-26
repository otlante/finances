package com.otlante.finances.data.remote

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.otlante.finances.ui.theme.PrimaryPurple
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsDataStore(private val context: Context) {

    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { it[DARK_THEME_KEY] ?: false }

    val primaryColor: Flow<Int> = context.dataStore.data
        .map { it[PRIMARY_COLOR_KEY] ?: DEFAULT_PRIMARY_COLOR }

    val syncIntervalHours: Flow<Int> = context.dataStore.data
        .map { it[SYNC_INTERVAL_HOURS_KEY] ?: DEFAULT_SYNC_INTERVAL_HOURS }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.dataStore.edit { it[DARK_THEME_KEY] = enabled }
    }

    suspend fun setPrimaryColor(color: Int) {
        context.dataStore.edit { it[PRIMARY_COLOR_KEY] = color }
    }

    suspend fun setSyncIntervalHours(hours: Int) {
        context.dataStore.edit { it[SYNC_INTERVAL_HOURS_KEY] = hours }
    }

    companion object {
        var DEFAULT_PRIMARY_COLOR = PrimaryPurple
        const val DEFAULT_SYNC_INTERVAL_HOURS = 6
        private const val DATASTORE_NAME = "settings_datastore"
        private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
        val PRIMARY_COLOR_KEY = intPreferencesKey("primary_color")
        val SYNC_INTERVAL_HOURS_KEY = intPreferencesKey("sync_interval_hours")
    }
} 