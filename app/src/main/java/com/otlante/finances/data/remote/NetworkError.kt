package com.otlante.finances.data.remote

/**
 * Represents different types of network errors that can occur during API calls.
 *
 * @property description a human-readable description of the error
 */
sealed class NetworkError(val description: String) {
    data class UnknownError(val throwable: Throwable? = null) : NetworkError("Unknown error")

    data object NoInternetError : NetworkError("No internet connection")

    data object ServerError : NetworkError("Server error")

}
