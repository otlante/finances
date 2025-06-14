package com.otlante.finances.model

data class IncomeItem(
    val emoji: String? = null,
    val title: String,
    val subtitle: String? = null,
    val amount: Double,
    val onClick: () -> Unit
)
