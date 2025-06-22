package com.otlante.finances

import android.app.Application
import com.otlante.finances.network.ApiClient
import com.otlante.finances.network.repository.ApiRepositoryImpl
import com.otlante.finances.domain.repository.ApiRepository

class MyApplication : Application() {

    lateinit var repository: ApiRepository
        private set

    override fun onCreate() {
        super.onCreate()

        val apiService = ApiClient.getApiService(applicationContext)
        repository = ApiRepositoryImpl(apiService)
    }
}