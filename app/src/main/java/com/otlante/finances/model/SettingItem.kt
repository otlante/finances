package com.otlante.finances.model

data class SettingItem(
    val title: String,
    val showArrow: Boolean = false,
    val showTrailing: Boolean = false,
    val showSwitch: Boolean = false,
    val onCheckedChange: ((Boolean) -> Unit)? = null,
    val onClick: (() -> Unit)? = null
)
