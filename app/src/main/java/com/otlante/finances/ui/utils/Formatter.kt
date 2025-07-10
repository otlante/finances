package com.otlante.finances.ui.utils

import com.otlante.finances.ui.utils.Formatter.Currency.RUB
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter


object Formatter {

    enum class Currency(val symbol: String, val description: String) {
        RUB("₽", "Российский рубль"),
        USD("$", "Американский доллар"),
        EUR("€", "Евро")
    }

    fun getCurrencySymbol(currencyCode: String?): String {
        return (Currency.entries.find { it.name == currencyCode } ?: RUB).symbol
    }

    /**
     * Formats a numeric amount as a currency string with Russian ruble suffix.
     */
    fun formatAmount(value: Double): String {
        return String.format("%.2f", value)
    }

    /**
     * Converts a string representation of a number to a formatted currency string.
     */
    fun formatAmount(value: String): String {
        val res = value.replace(",", ".").toDoubleOrNull()?.let {
            formatAmount(it)
        } ?: value
        return res
    }

    /**
     * Parses an ISO 8601 date-time string to [LocalDateTime].
     */
    fun formatDate(date: String): LocalDateTime =
        runCatching {
            LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
        }.getOrElse {
            error("Invalid date format: $date")
        }

    /**
     * Formats a [LocalDateTime] to a readable string with Russian month name and time.
     */
    fun formatTransactionDate(dateTime: LocalDateTime): String {
        val day = dateTime.dayOfMonth
        val month = monthInGenitive(dateTime.monthValue)
        val time = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        return "$day $month $time"
    }

    /**
     * Formats a [LocalDate] for display in headers, with Russian month name.
     */
    fun formatHeaderDate(date: LocalDate): String {
        val day = date.dayOfMonth
        val year = date.year
        val month = monthInGenitive(date.monthValue)
        return "$day $month $year"
    }

    /**
     * Returns the month name in Russian genitive case.
     */

    private fun monthInGenitive(month: Int): String = when (month) {
        1 -> "января"
        2 -> "февраля"
        3 -> "марта"
        4 -> "апреля"
        5 -> "мая"
        6 -> "июня"
        7 -> "июля"
        8 -> "августа"
        9 -> "сентября"
        10 -> "октября"
        11 -> "ноября"
        12 -> "декабря"
        else -> ""
    }
}
