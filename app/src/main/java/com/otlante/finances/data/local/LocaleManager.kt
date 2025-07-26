package com.otlante.finances.data.local

import android.content.Context
import android.content.res.Configuration
import com.otlante.finances.domain.entity.Language
import java.util.Locale

object LocaleManager {
    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }
}