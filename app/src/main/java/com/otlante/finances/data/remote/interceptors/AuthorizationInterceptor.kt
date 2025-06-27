package com.otlante.finances.data.remote.interceptors

import com.otlante.finances.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * An [Interceptor] that automatically adds an `Authorization` header
 * with a Bearer token to all outgoing HTTP requests.
 *
 * The token is retrieved from [BuildConfig.API_TOKEN].
 */
class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.API_TOKEN}")
            .build()
        return chain.proceed(request)
    }
}
