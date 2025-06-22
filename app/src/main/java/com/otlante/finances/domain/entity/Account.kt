package com.otlante.finances.domain.entity

data class Account(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String,
)