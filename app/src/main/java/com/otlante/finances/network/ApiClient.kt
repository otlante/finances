package com.otlante.finances.network

import android.content.Context
import com.otlante.finances.network.interceptors.AuthorizationInterceptor
import com.otlante.finances.network.interceptors.NetworkConnectionInterceptor
import com.otlante.finances.network.interceptors.RetryInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    @Volatile
    private var apiService: ApiService? = null

    fun getApiService(context: Context): ApiService {
        return apiService ?: synchronized(this) {
            apiService ?: createApiService(context).also { apiService = it }
        }
    }

    private fun createApiService(context: Context): ApiService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(NetworkConnectionInterceptor(context.applicationContext))
            .addInterceptor(AuthorizationInterceptor())
            .addInterceptor(RetryInterceptor())
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://shmr-finance.ru/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }
}