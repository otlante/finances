package com.otlante.finances.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.otlante.finances.R
import com.otlante.finances.model.SettingItem

@Composable
fun SettingsScreen() {

    val settingsItems = listOf(
        SettingItem(
            title = stringResource(R.string.dark_theme),
            showSwitch = true,
        ),
        SettingItem(
            title = stringResource(R.string.main_color),
            showTrailing = true,
        ),
        SettingItem(
            title = stringResource(R.string.sounds),
            showTrailing = true,
        ),
        SettingItem(
            title = stringResource(R.string.haptics),
            showTrailing = true,
        ),
        SettingItem(
            title = stringResource(R.string.code_password),
            showTrailing = true,
        ),
        SettingItem(
            title = stringResource(R.string.synchronization),
            showTrailing = true,
        ),
        SettingItem(
            title = stringResource(R.string.language),
            showTrailing = true,
        ),
        SettingItem(
            title = stringResource(R.string.about_the_program),
            showTrailing = true,
        )
    )

    Column {
        ScreenHeader(
            titleText = stringResource(R.string.settings),
        )
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
}
