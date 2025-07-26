package com.otlante.finances.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otlante.finances.data.remote.SettingsDataStore
import com.otlante.finances.data.remote.repository.PinCodeRepository
import com.otlante.finances.domain.entity.Language
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the settings screen state, including
 * the list of available settings and their behavior handlers.
 *
 * It exposes a [StateFlow] of [SettingItem] to allow the UI to observe
 * the current settings options.
 *
 * @constructor Initializes the ViewModel and loads the initial settings list.
 */
class SettingsViewModel @Inject constructor(
    val settingsDataStore: SettingsDataStore,
    private val pinCodeRepository: PinCodeRepository
) : ViewModel() {

    val isDarkTheme: StateFlow<Boolean> =
        settingsDataStore.isDarkTheme.stateIn(viewModelScope, SharingStarted.Lazily, false)
    val primaryColor: StateFlow<Int> =
        settingsDataStore.primaryColor.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            SettingsDataStore.DEFAULT_PRIMARY_COLOR
        )
    val syncIntervalHours: StateFlow<Int> =
        settingsDataStore.syncIntervalHours.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            SettingsDataStore.DEFAULT_SYNC_INTERVAL_HOURS
        )
    val language: StateFlow<Language> =
        settingsDataStore.language.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            SettingsDataStore.DEFAULT_LANGUAGE
        )
    val isPinSet: StateFlow<Boolean> = pinCodeRepository.isPinSet

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch { settingsDataStore.setDarkTheme(enabled) }
    }

    fun setPrimaryColor(color: Int) {
        viewModelScope.launch { settingsDataStore.setPrimaryColor(color) }
    }

    fun setSyncIntervalHours(hours: Int) {
        viewModelScope.launch { settingsDataStore.setSyncIntervalHours(hours) }
    }
}
