package com.otlante.finances.data.remote

/**
 * A sealed class representing the result of an operation,
 * which can either be a success containing data, or an error.
 *
 * @param T the type of the successful result data
 */
sealed class ResultState<out T> {
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val error: NetworkError) : ResultState<Nothing>()

    /**
     * Executes one of the given functions depending on whether
     * the result is a success or an error.
     *
     * @param onSuccess function to invoke if this is a [Success]
     * @param onError function to invoke if this is an [Error]
     */
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
