package com.otlante.finances

import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import java.util.Locale

fun formatAmount(value: Double): String {
    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }
    val df = DecimalFormat("#,##0.##", symbols)
    return "${df.format(value)} ₽"
}