package com.otlante.finances.domain.entity

/**
 * Represents a transaction category used to classify expenses or incomes.
 *
 * @property id unique identifier of the category
 * @property name display name of the category
 * @property emoji emoji symbol representing the category visually
 * @property isIncome flag indicating whether this category is for income (true) or expense (false)
 */
data class Category(
    val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
)
