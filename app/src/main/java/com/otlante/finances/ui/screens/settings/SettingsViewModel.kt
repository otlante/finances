package com.otlante.finances.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.otlante.finances.data.remote.SettingsDataStore
import com.otlante.finances.domain.entity.SettingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    val isDarkTheme: StateFlow<Boolean> =
        settingsDataStore.isDarkTheme.stateIn(viewModelScope, SharingStarted.Lazily, false)
    val primaryColor: StateFlow<Int> =
        settingsDataStore.primaryColor.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            SettingsDataStore.DEFAULT_PRIMARY_COLOR
        )

    private val _settingsItems = MutableStateFlow<List<SettingItem>>(emptyList())
    val settingsItems: StateFlow<List<SettingItem>> = _settingsItems.asStateFlow()

    init {
        viewModelScope.launch {
            _settingsItems.value = listOf(
                SettingItem(
                    title = "Тёмная тема",
                    showSwitch = true,
                    onCheckedChange = { setDarkTheme(it) }
                ),
                SettingItem(
                    title = "Основной цвет",
                    showTrailing = true,
                    onClick = { }
                ),
                SettingItem(
                    title = "Звуки",
                    showTrailing = true,
                    onClick = { }
                ),
                SettingItem(
                    title = "Хаптики",
                    showTrailing = true,
                    onClick = { }
                ),
                SettingItem(
                    title = "Код пароль",
                    showTrailing = true,
                    onClick = { }
                ),
                SettingItem(
                    title = "Синхронизация",
                    showTrailing = true,
                    onClick = { }
                ),
                SettingItem(
                    title = "Язык",
                    showTrailing = true,
                    onClick = { }
                ),
                SettingItem(
                    title = "О программе",
                    showTrailing = true,
                    onClick = { }
                )
            )
        }
    }

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch { settingsDataStore.setDarkTheme(enabled) }
    }

    fun setPrimaryColor(color: Int) {
        viewModelScope.launch { settingsDataStore.setPrimaryColor(color) }
    }
}
