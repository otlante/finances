package com.otlante.finances.network

sealed class NetworkError(val description: String) {
    data class UnknownError(val throwable: Throwable? = null) : NetworkError("Unknown error")

    data object NoInternetError : NetworkError("No internet connection")

    data object ServerError : NetworkError("Server error")

}