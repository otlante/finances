package com.otlante.finances.ui.screens.settings

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otlante.finances.data.remote.repository.VibrationSettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class VibrationMode {
    SHORT, MEDIUM, LONG, PRIMITIVE_LOW_TICK, PRIMITIVE_SPIN, PRIMITIVE_THUD
}

class VibrationSettingsViewModel(
    context: Context
) : ViewModel() {
    private val repository = VibrationSettingsRepository(context)

    val isVibrationEnabled: StateFlow<Boolean> = repository.isVibrationEnabled.stateIn(viewModelScope, SharingStarted.Lazily, true)
    val vibrationMode: StateFlow<VibrationMode> = repository.vibrationMode.stateIn(viewModelScope, SharingStarted.Lazily, VibrationMode.SHORT)

    fun setVibrationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setVibrationEnabled(enabled)
        }
    }

    fun setVibrationMode(mode: VibrationMode) {
        viewModelScope.launch {
            repository.setVibrationMode(mode)
        }
    }
} 