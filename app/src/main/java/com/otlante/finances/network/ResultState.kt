package com.otlante.finances.network

sealed class ResultState<out T> {
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val error: NetworkError) : ResultState<Nothing>()

    inline fun fold(
        onSuccess: (T) -> Unit,
        onError: (NetworkError) -> Unit
    ) {
        when (this) {
            is Success -> onSuccess(data)
            is Error -> onError(error)
        }
    }
}