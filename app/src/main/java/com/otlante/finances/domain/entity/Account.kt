package com.otlante.finances.domain.entity

/**
 * Represents a user account with an identifier, name, balance, and currency.
 *
 * @property id unique identifier of the account
 * @property name display name of the account
 * @property balance current balance as a string (e.g., formatted with decimals)
 * @property currency currency code of the account balance (e.g., "USD", "EUR")
 */
data class Account(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String,
)
