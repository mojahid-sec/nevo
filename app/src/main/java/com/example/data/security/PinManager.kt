package com.example.data.security

import android.content.Context
import android.content.SharedPreferences
import java.security.MessageDigest
import java.security.SecureRandom

class PinManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("nivo_security_prefs", Context.MODE_PRIVATE)

    fun isPinEnabled(): Boolean {
        return prefs.getBoolean(KEY_PIN_ENABLED, false) && hasPin()
    }

    fun hasPin(): Boolean {
        return !prefs.getString(KEY_PIN_HASH, null).isNullOrEmpty()
    }

    fun setPin(pin: String) {
        val salt = generateSalt()
        val hash = hashPin(pin, salt)
        prefs.edit()
            .putString(KEY_PIN_SALT, salt)
            .putString(KEY_PIN_HASH, hash)
            .putBoolean(KEY_PIN_ENABLED, true)
            .apply()
    }

    fun verifyPin(pin: String): Boolean {
        val storedHash = prefs.getString(KEY_PIN_HASH, null) ?: return false
        val storedSalt = prefs.getString(KEY_PIN_SALT, null) ?: return false
        val computedHash = hashPin(pin, storedSalt)
        return storedHash == computedHash
    }

    fun disablePin() {
        prefs.edit()
            .remove(KEY_PIN_HASH)
            .remove(KEY_PIN_SALT)
            .putBoolean(KEY_PIN_ENABLED, false)
            .apply()
    }

    private fun generateSalt(): String {
        val random = SecureRandom()
        val saltBytes = ByteArray(16)
        random.nextBytes(saltBytes)
        return saltBytes.joinToString("") { "%02x".format(it) }
    }

    private fun hashPin(pin: String, salt: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val input = salt + pin
        val digest = md.digest(input.toByteArray(Charsets.UTF_8))
        return digest.joinToString("") { "%02x".format(it) }
    }

    companion object {
        private const val KEY_PIN_ENABLED = "pin_lock_enabled"
        private const val KEY_PIN_HASH = "pin_sha256_hash"
        private const val KEY_PIN_SALT = "pin_salt"

        @Volatile
        private var INSTANCE: PinManager? = null

        fun getInstance(context: Context): PinManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PinManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
