package com.otlante.finances.domain.entity

enum class Language(val code: String, val displayName: String) {
    RUSSIAN("ru", "Русский"),
    ENGLISH("en", "English");

    companion object {
        fun fromCode(code: String): Language {
            return values().find { it.code == code } ?: RUSSIAN
        }
    }
} 