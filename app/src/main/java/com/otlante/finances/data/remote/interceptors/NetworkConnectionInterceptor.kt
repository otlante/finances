package com.otlante.finances.data.remote.interceptors

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import com.otlante.finances.data.remote.NoConnectionException
import okhttp3.Interceptor
import okhttp3.Response

/**
 * An [Interceptor] that checks network availability before
 * proceeding with HTTP requests.
 *
 * If there is no active network connection, a [NoConnectionException]
 * is thrown to prevent the request from being executed.
 *
 * @property context the Android [Context] used to access network services
 */
class NetworkConnectionInterceptor(
    private val context: Context
) : Interceptor {
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkAvailable()) {
            throw NoConnectionException()
        }
        return chain.proceed(chain.request())
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val activeNetwork = connectivityManager.getNetworkCapabilities(network)
        return activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false ||
                activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
    }
}
