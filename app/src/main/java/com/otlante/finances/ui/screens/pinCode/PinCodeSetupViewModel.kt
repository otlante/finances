package com.otlante.finances.ui.screens.pinCode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otlante.finances.data.remote.repository.PinCodeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PinCodeSetupUiState(
    val pinCode: String = "",
    val step: PinSetupStep = PinSetupStep.FIRST_ENTER,
    val firstPin: String = "",
    val errorMessage: String = "",
    val isPinSet: Boolean = false
)

class PinCodeSetupViewModel @Inject constructor(
    private val pinCodeRepository: PinCodeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PinCodeSetupUiState())
    val uiState: StateFlow<PinCodeSetupUiState> = _uiState.asStateFlow()

    fun updatePinCode(pin: String) {
        _uiState.value = _uiState.value.copy(
            pinCode = pin,
            errorMessage = ""
        )
    }

    fun confirmPin() {
        val currentState = _uiState.value
        
        when (currentState.step) {
            PinSetupStep.FIRST_ENTER -> {
                if (currentState.pinCode.length == 4) {
                    _uiState.value = currentState.copy(
                        firstPin = currentState.pinCode,
                        pinCode = "",
                        step = PinSetupStep.CONFIRM
                    )
                }
            }
            PinSetupStep.CONFIRM -> {
                if (currentState.pinCode == currentState.firstPin) {
                    viewModelScope.launch {
                        val success = pinCodeRepository.setPin(currentState.pinCode)
                        if (success) {
                            _uiState.value = currentState.copy(isPinSet = true)
                        } else {
                            _uiState.value = currentState.copy(
                                errorMessage = "Ошибка при сохранении PIN-кода"
                            )
                        }
                    }
                } else {
                    _uiState.value = currentState.copy(
                        errorMessage = "PIN-коды не совпадают",
                        pinCode = ""
                    )
                }
            }
        }
    }

    fun reset() {
        _uiState.value = PinCodeSetupUiState()
    }
} 