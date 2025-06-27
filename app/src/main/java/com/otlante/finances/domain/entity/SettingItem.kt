package com.otlante.finances.domain.entity

/**
 * Data class representing a single setting item in the settings screen.
 *
 * @property title The display title of the setting.
 * @property showArrow Whether to show a navigation arrow (default false).
 * @property showTrailing Whether to show a trailing icon or indicator (default false).
 * @property showSwitch Whether to show a toggle switch (default false).
 * @property onCheckedChange Optional callback triggered when switch is toggled.
 * @property onClick Optional callback triggered when the item is clicked.
 */
data class SettingItem(
    val title: String,
    val showArrow: Boolean = false,
    val showTrailing: Boolean = false,
    val showSwitch: Boolean = false,
    val onCheckedChange: ((Boolean) -> Unit)? = null,
    val onClick: (() -> Unit)? = null
)
