package com.otlante.finances.di.module

import android.content.Context
import com.otlante.finances.data.remote.ApiService
import com.otlante.finances.data.remote.interceptors.AuthorizationInterceptor
import com.otlante.finances.data.remote.interceptors.NetworkConnectionInterceptor
import com.otlante.finances.data.remote.interceptors.RetryInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object ApiModule {

    private const val BASIC_TIMEOUT = 15L
    private const val BASE_URL = "https://shmr-finance.ru/api/v1/"

    @Provides
    @Singleton
    fun provideOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(NetworkConnectionInterceptor(context.applicationContext))
            .addInterceptor(AuthorizationInterceptor())
            .addInterceptor(RetryInterceptor())
            .connectTimeout(BASIC_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(BASIC_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}