package com.otlante.finances.data.remote

import android.content.Context
import com.otlante.finances.data.remote.interceptors.AuthorizationInterceptor
import com.otlante.finances.data.remote.interceptors.NetworkConnectionInterceptor
import com.otlante.finances.data.remote.interceptors.RetryInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton responsible for providing a configured instance of [ApiService].
 *
 * This client uses OkHttp with interceptors for:
 * - network connectivity checking ([NetworkConnectionInterceptor])
 * - automatic authorization ([AuthorizationInterceptor])
 * - request retrying ([RetryInterceptor])
 *
 * Ensures a single instance of [ApiService] is created and reused across the app.
 */
object ApiClient {

    private const val BASIC_TIMEOUT = 15L
    private const val BASE_URL = "https://shmr-finance.ru/api/v1/"

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
            .connectTimeout(BASIC_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(BASIC_TIMEOUT, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }
}
