package com.otlante.finances.data.local

import android.content.Context
import android.content.res.Configuration
import com.otlante.finances.domain.entity.Language
import java.util.Locale

//class LocaleManager(private val context: Context) {
//
//    fun setLocale(language: Language) {
//        val locale = Locale(language.code)
//        Locale.setDefault(locale)
//
//        val configuration = Configuration(context.resources.configuration)
//        configuration.setLocale(locale)
//
//        context.createConfigurationContext(configuration)
//        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
//    }
//}

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