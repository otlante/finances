package com.otlante.finances.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.otlante.finances.R

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
                title = { Text(stringResource(R.string.vibration_settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
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
                Text(stringResource(R.string.vibration_enabled), modifier = Modifier.weight(1f))
                Switch(checked = isEnabled, onCheckedChange = viewModel::setVibrationEnabled)
            }
            Text(stringResource(R.string.vibration_mode))
            VibrationMode.entries.forEach { vibrationMode ->
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