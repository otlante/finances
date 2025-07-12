package com.otlante.finances.domain.entity

data class EditTransactionRequest(
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String
)
