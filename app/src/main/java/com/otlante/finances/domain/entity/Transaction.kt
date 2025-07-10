package com.otlante.finances.domain.entity

/**
 * Represents a financial transaction with details such as category, amount, and date.
 *
 * @property id unique identifier of the transaction
 * @property category the [Category] this transaction belongs to
 * @property amount the amount involved in the transaction as a formatted string
 * @property transactionDate the date of the transaction in ISO string format
 * @property comment optional comment or note attached to the transaction
 */
data class Transaction(
    val id: Int,
    val account: Account,
    val category: Category,
    val amount: String,
    val transactionDate: String,
    val comment: String? = null
)
