package com.otlante.finances

import android.app.Application
import com.otlante.finances.data.remote.ApiClient
import com.otlante.finances.data.remote.repository.ApiRepositoryImpl
import com.otlante.finances.domain.repository.ApiRepository

/**
 * Application class used for setting up application-wide dependencies.
 *
 * Initializes the [ApiRepository] instance to be shared across the app.
 */
class MyApplication : Application() {

    lateinit var repository: ApiRepository
        private set

    override fun onCreate() {
        super.onCreate()

        val apiService = ApiClient.getApiService(applicationContext)
        repository = ApiRepositoryImpl(apiService)
    }
}
