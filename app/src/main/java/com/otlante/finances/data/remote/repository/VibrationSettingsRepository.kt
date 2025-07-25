package com.otlante.finances.data.remote.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.otlante.finances.ui.screens.settings.VibrationMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VibrationSettingsRepository(private val context: Context) {
    companion object {
        private val Context.dataStore by preferencesDataStore(name = "vibration_settings")
        private val KEY_ENABLED = booleanPreferencesKey("vibration_enabled")
        private val KEY_MODE = stringPreferencesKey("vibration_mode")
    }

    val isVibrationEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_ENABLED] ?: true
    }

    val vibrationMode: Flow<VibrationMode> = context.dataStore.data.map { prefs ->
        prefs[KEY_MODE]?.let { runCatching { VibrationMode.valueOf(it) }.getOrNull() } ?: VibrationMode.SHORT
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ENABLED] = enabled
        }
    }

    suspend fun setVibrationMode(mode: VibrationMode) {
        context.dataStore.edit { prefs ->
            prefs[KEY_MODE] = mode.name
        }
    }
} 