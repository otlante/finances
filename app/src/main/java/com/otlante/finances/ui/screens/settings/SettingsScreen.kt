package com.otlante.finances.ui.screens.settings

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.otlante.finances.ui.composition.LocalViewModelFactory
import com.otlante.finances.domain.entity.SettingItem
import com.otlante.finances.ui.components.ListItem

/**
 * Composable screen displaying a list of settings items with switches or trailing icons.
 *
 * Each setting is represented by a [SettingItem], which can optionally show a switch,
 * a trailing arrow, and handle click or toggle events.
 */
@Composable
fun SettingsScreen() {

    val factory = LocalViewModelFactory.current
    val viewModel: SettingsViewModel = viewModel(factory = factory)

    val settingsItems by viewModel.settingsItems.collectAsState()
    LazyColumn {
        items(settingsItems) { item ->
            ListItem(
                title = item.title,
                showArrow = item.showArrow,
                showTrailing = item.showTrailing,
                showSwitch = item.showSwitch,
                onCheckedChange = item.onCheckedChange,
                onClick = item.onClick
            )
            HorizontalDivider()
        }
    }
}
