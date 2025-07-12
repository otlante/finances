package com.otlante.finances

import android.app.Application
import android.content.Context
import com.otlante.finances.di.component.AppComponent
import com.otlante.finances.di.component.DaggerAppComponent

/**
 * Application class used for setting up application-wide dependencies.
 *
 * Initializes the [AppComponent] instance to be shared across the app.
 */

class MyApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(this)
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is MyApplication -> appComponent
        else -> applicationContext.appComponent
    }
