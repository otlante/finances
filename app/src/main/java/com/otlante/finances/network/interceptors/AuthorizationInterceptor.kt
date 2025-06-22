package com.otlante.finances.network.interceptors

import com.otlante.finances.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.API_TOKEN}")
            .build()
        return chain.proceed(request)
    }
}