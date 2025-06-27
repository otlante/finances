package com.otlante.finances.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * An [Interceptor] that automatically retries HTTP requests
 * when server errors (HTTP 5xx) or network exceptions occur.
 *
 * Retries are performed with a fixed delay of [RETRY_DELAY_MS]
 * between attempts, up to [MAX_RETRIES_COUNT] times.
 */
class RetryInterceptor : Interceptor {

    companion object {
        private const val RETRY_DELAY_MS = 2000L
        private const val MAX_RETRIES_COUNT = 3
        private const val SERVER_ERROR_START_CODE = 500
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response? = null
        var exception: IOException? = null
        var tryCount = 0

        while (shouldRetry(tryCount, response)) {
            response?.close()

            try {
                response = chain.proceed(request)
                if (response.isSuccessful || response.code < SERVER_ERROR_START_CODE) {
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

    private fun shouldRetry(tryCount: Int, response: Response?): Boolean {
        return tryCount < MAX_RETRIES_COUNT &&
                (response == null || !response.isSuccessful && response.code >= SERVER_ERROR_START_CODE)
    }
}
