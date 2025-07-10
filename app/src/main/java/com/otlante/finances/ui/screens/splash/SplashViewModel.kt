package com.otlante.finances.ui.screens.splash

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SplashViewModel @Inject constructor() : ViewModel() {
    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady

    fun markReady() {
        _isReady.value = true
    }
}
