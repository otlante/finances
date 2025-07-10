package com.otlante.finances.domain.entity

data class UpdateAccountRequest(
    val name: String,
    val balance: String,
    val currency: String
)
