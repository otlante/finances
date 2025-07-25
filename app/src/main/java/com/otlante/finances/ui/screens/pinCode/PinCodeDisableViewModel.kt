package com.otlante.finances.ui.screens.pinCode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otlante.finances.data.remote.repository.PinCodeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PinCodeDisableViewModel @Inject constructor(
    private val pinCodeRepository: PinCodeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PinCodeDisableUiState())
    val uiState: StateFlow<PinCodeDisableUiState> = _uiState.asStateFlow()

    fun updatePinCode(pin: String) {
        _uiState.value = _uiState.value.copy(
            pinCode = pin,
            errorMessage = ""
        )
    }

    fun verifyAndDisablePin() {
        val currentState = _uiState.value
        
        if (currentState.pinCode.length == 4) {
            val isCorrect = pinCodeRepository.verifyPin(currentState.pinCode)
            
            if (isCorrect) {
                viewModelScope.launch {
                    val success = pinCodeRepository.removePin()
                    if (success) {
                        _uiState.value = currentState.copy(isPinDisabled = true)
                    } else {
                        _uiState.value = currentState.copy(
                            errorMessage = "Ошибка при отключении PIN-кода"
                        )
                    }
                }
            } else {
                _uiState.value = currentState.copy(
                    errorMessage = "Неверный PIN-код",
                    pinCode = ""
                )
            }
        }
    }

    fun reset() {
        _uiState.value = PinCodeDisableUiState()
    }
}

data class PinCodeDisableUiState(
    val pinCode: String = "",
    val errorMessage: String = "",
    val isPinDisabled: Boolean = false
)