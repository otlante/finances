package com.otlante.finances.ui.screens.pinCode

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.otlante.finances.ui.composition.LocalViewModelFactory

@Composable
fun PinCodeSetupScreen(
    onBack: () -> Unit,
    onPinSet: () -> Unit
) {
    val factory = LocalViewModelFactory.current
    val viewModel: PinCodeSetupViewModel = viewModel(factory = factory)
    
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(uiState.isPinSet) {
        if (uiState.isPinSet) {
            onPinSet()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = when (uiState.step) {
                PinSetupStep.FIRST_ENTER -> "Установите PIN-код"
                PinSetupStep.CONFIRM -> "Подтвердите PIN-код"
            },
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = when (uiState.step) {
                PinSetupStep.FIRST_ENTER -> "Введите 4-значный PIN-код"
                PinSetupStep.CONFIRM -> "Повторите PIN-код для подтверждения"
            },
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = uiState.pinCode,
            onValueChange = { 
                if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                    viewModel.updatePinCode(it)
                }
            },
            label = { Text("PIN-код") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        
        if (uiState.errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = uiState.errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Отмена")
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Button(
                onClick = { viewModel.confirmPin() },
                enabled = uiState.pinCode.length == 4,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    when (uiState.step) {
                        PinSetupStep.FIRST_ENTER -> "Далее"
                        PinSetupStep.CONFIRM -> "Установить"
                    }
                )
            }
        }
    }
}

enum class PinSetupStep {
    FIRST_ENTER,
    CONFIRM
} 