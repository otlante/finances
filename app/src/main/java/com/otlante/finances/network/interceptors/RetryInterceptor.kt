package com.otlante.finances.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RetryInterceptor : Interceptor {

    companion object {
        private const val RETRY_DELAY_MS = 2000L
        private const val MAX_RETRIES_COUNT = 3
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response? = null
        var exception: IOException? = null
        var tryCount = 0

        while (tryCount < MAX_RETRIES_COUNT && (response == null || !response.isSuccessful && response.code >= 500)) {
            response?.close()

            try {
                response = chain.proceed(request)
                if (response.isSuccessful || response.code < 500) {
                    return response
                }
            } catch (e: IOException) {
                exception = e
            }

            tryCount++

            if (tryCount < MAX_RETRIES_COUNT) {
                try {
                    Thread.sleep(RETRY_DELAY_MS)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    throw IOException(e)
                }
            }
        }
        return response ?: throw exception ?: IOException("Unknown error")
    }
}