package com.otlante.finances.data.remote

import java.io.IOException

/**
 * Exception thrown when there is no active internet connection
 * available to perform a network request.
 */
class NoConnectionException : IOException("No internet connection")
