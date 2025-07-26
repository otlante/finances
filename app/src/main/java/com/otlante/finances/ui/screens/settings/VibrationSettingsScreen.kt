package com.otlante.finances.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
            Text(
                text = stringResource(R.string.vibration_settings_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.vibration_enabled), modifier = Modifier.weight(1f))
            Switch(checked = isEnabled, onCheckedChange = viewModel::setVibrationEnabled)
        }
        Text(modifier = Modifier.padding(16.dp), text = stringResource(R.string.vibration_mode))
        VibrationMode.entries.forEach { vibrationMode ->
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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