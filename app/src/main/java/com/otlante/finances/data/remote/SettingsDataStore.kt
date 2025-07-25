package com.otlante.finances.data.remote

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.otlante.finances.data.remote.SettingsDataStore.Companion.DARK_THEME_KEY
import com.otlante.finances.data.remote.SettingsDataStore.Companion.DATASTORE_NAME
import com.otlante.finances.data.remote.SettingsDataStore.Companion.PRIMARY_COLOR_KEY
import com.otlante.finances.data.remote.SettingsDataStore.Companion.dataStore
import com.otlante.finances.ui.theme.PrimaryPurple
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsDataStore(private val context: Context) {

    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { it[DARK_THEME_KEY] ?: false }

    val primaryColor: Flow<Int> = context.dataStore.data
        .map { it[PRIMARY_COLOR_KEY] ?: DEFAULT_PRIMARY_COLOR }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.dataStore.edit { it[DARK_THEME_KEY] = enabled }
    }

    suspend fun setPrimaryColor(color: Int) {
        context.dataStore.edit { it[PRIMARY_COLOR_KEY] = color }
    }

    companion object {
        var DEFAULT_PRIMARY_COLOR = PrimaryPurple
        private const val DATASTORE_NAME = "settings_datastore"
        private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
        val PRIMARY_COLOR_KEY = intPreferencesKey("primary_color")
    }
} 