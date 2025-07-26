package com.otlante.finances.ui.screens.pinCode

import androidx.lifecycle.ViewModel
import com.otlante.finances.data.remote.repository.PinCodeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class PinCodeEntryViewModel @Inject constructor(
    private val pinCodeRepository: PinCodeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PinCodeEntryUiState())
    val uiState: StateFlow<PinCodeEntryUiState> = _uiState.asStateFlow()
    
    val isPinSet: StateFlow<Boolean> = pinCodeRepository.isPinSet

    fun updatePinCode(pin: String) {
        _uiState.value = _uiState.value.copy(
            pinCode = pin,
            errorMessage = ""
        )
    }

    fun verifyPin() {
        val currentState = _uiState.value
        
        if (currentState.pinCode.length == 4) {
            val isCorrect = pinCodeRepository.verifyPin(currentState.pinCode)
            
            if (isCorrect) {
                _uiState.value = currentState.copy(isPinCorrect = true)
            } else {
                _uiState.value = currentState.copy(
                    errorMessage = "Неверный PIN-код",
                    pinCode = ""
                )
            }
        }
    }

    fun reset() {
        _uiState.value = PinCodeEntryUiState()
    }
} 