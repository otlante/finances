package com.otlante.finances.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.otlante.finances.domain.entity.SettingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel responsible for managing the settings screen state, including
 * the list of available settings and their behavior handlers.
 *
 * It exposes a [StateFlow] of [SettingItem] to allow the UI to observe
 * the current settings options.
 *
 * @constructor Initializes the ViewModel and loads the initial settings list.
 */
class SettingsViewModel : ViewModel() {

    private val _settingsItems = MutableStateFlow<List<SettingItem>>(emptyList())
    val settingsItems: StateFlow<List<SettingItem>> = _settingsItems.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        _settingsItems.value = listOf(
            SettingItem(
                title = "Тёмная тема",
                showSwitch = true,
                onCheckedChange = { }
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

/**
 * Factory for creating instances of [SettingsViewModel].
 */
class SettingsViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
