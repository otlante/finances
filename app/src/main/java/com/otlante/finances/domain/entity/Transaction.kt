package com.otlante.finances.domain.entity

data class Transaction(
    val id: Int,
    val category: Category,
    val amount: String,
    val transactionDate: String,
    val comment: String? = null
)