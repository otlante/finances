package com.otlante.finances.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import com.otlante.finances.ui.components.ListItem

@Composable
fun SettingsScreen() {

    val settingsItems = listOf(
        SettingItem(
            title = "Тёмная тема",
            showSwitch = true,
        ),
        SettingItem(
            title = "Основной цвет",
            showTrailing = true,
            onClick = { }
        ),
        SettingItem(
            title = "Звуки",
            showTrailing = true,
            onClick = { }
        ),
        SettingItem(
            title = "Хаптики",
            showTrailing = true,
            onClick = { }
        ),
        SettingItem(
            title = "Код пароль",
            showTrailing = true,
            onClick = { }
        ),
        SettingItem(
            title = "Синхронизация",
            showTrailing = true,
            onClick = { }
        ),
        SettingItem(
            title = "Язык",
            showTrailing = true,
            onClick = { }
        ),
        SettingItem(
            title = "О программе",
            showTrailing = true,
            onClick = { }
        )
    )

    Column {
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

data class SettingItem(
    val title: String,
    val showArrow: Boolean = false,
    val showTrailing: Boolean = false,
    val showSwitch: Boolean = false,
    val onCheckedChange: ((Boolean) -> Unit)? = null,
    val onClick: (() -> Unit)? = null
)