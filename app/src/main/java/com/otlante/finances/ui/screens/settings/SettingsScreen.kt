@file:OptIn(ExperimentalMaterial3Api::class)

package com.otlante.finances.ui.screens.settings

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.otlante.finances.MainActivity
import com.otlante.finances.R
import com.otlante.finances.domain.entity.Language
import com.otlante.finances.ui.components.ListItem
import com.otlante.finances.ui.composition.LocalViewModelFactory
import kotlinx.coroutines.launch

/**
 * Composable screen displaying a list of settings items with switches or trailing icons.
 *
 * Each setting is represented by a [SettingItem], which can optionally show a switch,
 * a trailing arrow, and handle click or toggle events.
 */
@Composable
fun SettingsScreen(
    onVibrationClick: () -> Unit,
    onPrimaryColorClick: () -> Unit,
    onPinCodeClick: () -> Unit,
    onPinCodeDisableClick: () -> Unit,
    onSyncClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    var showLanguageSelection by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val factory = LocalViewModelFactory.current
    val viewModel: SettingsViewModel = viewModel(factory = factory)

    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val isPinSet by viewModel.isPinSet.collectAsState()
    val currentLanguage by viewModel.language.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn {
        item {
            ListItem(
                title = stringResource(R.string.settings_dark_theme),
                showSwitch = true,
                initialSwitchState = isDarkTheme,
                onCheckedChange = viewModel::setDarkTheme
            )
            HorizontalDivider()
        }

        item {
            ListItem(
                title = stringResource(R.string.settings_primary_color),
                showTrailing = true,
                onClick = onPrimaryColorClick
            )
            HorizontalDivider()
        }

        item {
            ListItem(
                title = stringResource(R.string.settings_haptics),
                showTrailing = true,
                onClick = onVibrationClick
            )
            HorizontalDivider()
        }

        item {
            ListItem(
                title = stringResource(R.string.settings_pin_code),
                showTrailing = true,
                onClick = if (isPinSet) onPinCodeDisableClick else onPinCodeClick
            )
            HorizontalDivider()
        }

        item {
            ListItem(
                title = stringResource(R.string.settings_sync),
                showTrailing = true,
                onClick = onSyncClick
            )
            HorizontalDivider()
        }

        item {
            ListItem(
                title = stringResource(R.string.settings_language),
                showTrailing = true,
                onClick = { showLanguageSelection = true }
            )
            HorizontalDivider()
        }

        item {
            ListItem(
                title = stringResource(R.string.settings_about),
                showTrailing = true,
                onClick = onAboutClick
            )
            HorizontalDivider()
        }
    }

    if (showLanguageSelection) {
        val sheetState = rememberModalBottomSheetState()

        ModalBottomSheet(
            onDismissRequest = { showLanguageSelection = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.language_selection_title),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Language.entries.forEach { language ->
                    ListItem(
                        title = language.displayName,
                        showTrailing = currentLanguage == language,
                        onClick = {
                            if (currentLanguage != language) {
                                coroutineScope.launch {
//                                    viewModel.setLanguage(language)
                                    viewModel.settingsDataStore.setLanguage(language)
                                    showLanguageSelection = false

                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(intent)
                                    if (context is Activity) context.finish()
                                }
                            }
                        }
                    )
                    HorizontalDivider()
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
