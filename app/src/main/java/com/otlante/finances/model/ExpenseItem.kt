package com.otlante.finances.model

data class ExpenseItem(
    val emoji: String,
    val title: String,
    val subtitle: String? = null,
    val amount: Double,
    val onClick: () -> Unit
)
