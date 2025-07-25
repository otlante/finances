package com.otlante.finances.data.remote.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PinCodeRepository @Inject constructor(
    private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedSharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "pin_code_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val _isPinSet = MutableStateFlow(false)
    val isPinSet: StateFlow<Boolean> = _isPinSet.asStateFlow()

    init {
        _isPinSet.value = encryptedSharedPreferences.contains(PIN_KEY)
    }

    fun setPin(pin: String): Boolean {
        return try {
            encryptedSharedPreferences.edit()
                .putString(PIN_KEY, pin)
                .apply()
            _isPinSet.value = true
            true
        } catch (e: Exception) {
            false
        }
    }

    fun verifyPin(pin: String): Boolean {
        val storedPin = encryptedSharedPreferences.getString(PIN_KEY, null)
        return storedPin == pin
    }

    fun removePin(): Boolean {
        return try {
            encryptedSharedPreferences.edit()
                .remove(PIN_KEY)
                .apply()
            _isPinSet.value = false
            true
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        private const val PIN_KEY = "pin_code"
    }
} 