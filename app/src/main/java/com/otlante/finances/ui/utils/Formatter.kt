package com.otlante.finances.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import com.otlante.finances.R
import com.otlante.finances.ui.utils.Formatter.Currency.RUB
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


object Formatter {

    enum class Currency(val symbol: String, val descriptionResId: Int) {
        RUB("₽", R.string.currency_rub),
        USD("$", R.string.currency_usd),
        EUR("€", R.string.currency_eur)
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

    fun formatTransactionDate(context: Context, dateTime: LocalDateTime): String {
        val day = dateTime.dayOfMonth
        val month = monthInGenitive(context, dateTime.monthValue)
        val time = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        return "$day $month $time"
    }

    fun formatHeaderDate(context: Context, date: LocalDate): String {
        val day = date.dayOfMonth
        val year = date.year
        val month = monthInGenitive(context, date.monthValue)
        return "$day $month $year"
    }

    @SuppressLint("DefaultLocale")
    fun formatHeaderTime(time: LocalTime): String {
        val hours = time.hour
        val minutes = time.minute
        return String.format("%02d:%02d", hours, minutes)
    }

    fun formatToIsoUtc(localDate: LocalDate, localTime: LocalTime): String {
        val localDateTime = localDate.atTime(localTime)
        val utcZoned = localDateTime.atZone(ZoneOffset.UTC)
        return utcZoned.format(DateTimeFormatter.ISO_INSTANT)
    }

    private fun monthInGenitive(context: Context, month: Int): String = when (month) {
        1 -> context.getString(R.string.month_january)
        2 -> context.getString(R.string.month_february)
        3 -> context.getString(R.string.month_march)
        4 -> context.getString(R.string.month_april)
        5 -> context.getString(R.string.month_may)
        6 -> context.getString(R.string.month_june)
        7 -> context.getString(R.string.month_july)
        8 -> context.getString(R.string.month_august)
        9 -> context.getString(R.string.month_september)
        10 -> context.getString(R.string.month_october)
        11 -> context.getString(R.string.month_november)
        12 -> context.getString(R.string.month_december)
        else -> ""
    }
}
