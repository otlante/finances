package com.otlante.finances.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VibrationSettingsScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current.applicationContext
    val viewModel: VibrationSettingsViewModel = viewModel(
        factory = viewModelFactory {
            initializer { VibrationSettingsViewModel(context) }
        }
    )
    val isEnabled by viewModel.isVibrationEnabled.collectAsState()
    val mode by viewModel.vibrationMode.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки вибрации") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Вибрация", modifier = Modifier.weight(1f))
                Switch(checked = isEnabled, onCheckedChange = viewModel::setVibrationEnabled)
            }
            Text("Режим вибрации:")
            VibrationMode.values().forEach { vibrationMode ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = mode == vibrationMode,
                        onClick = { viewModel.setVibrationMode(vibrationMode) },
                        enabled = isEnabled
                    )
                    Text(vibrationMode.name)
                }
            }
        }
    }
} 