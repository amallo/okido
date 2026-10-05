package com.babatunde.okido.infra

import android.content.SharedPreferences
import com.babatunde.okido.core.ParentPin
import java.security.MessageDigest
import java.security.SecureRandom

/** Keeps only a salted SHA-256 hash of the parent PIN, never the PIN itself. */
class SharedPreferencesParentPin(private val preferences: SharedPreferences) : ParentPin {
    override fun verify(pin: String): Boolean {
        val salt = preferences.getString(KEY_SALT, null) ?: return false
        val hash = preferences.getString(KEY_HASH, null) ?: return false
        return MessageDigest.isEqual(hash(salt, pin).toByteArray(), hash.toByteArray())
    }

    override fun isSet(): Boolean = preferences.contains(KEY_HASH)

    override fun save(pin: String) {
        val salt = ByteArray(SALT_SIZE).also { SecureRandom().nextBytes(it) }.toHex()
        preferences.edit()
            .putString(KEY_SALT, salt)
            .putString(KEY_HASH, hash(salt, pin))
            .apply()
    }

    private fun hash(salt: String, pin: String): String =
        MessageDigest.getInstance("SHA-256").digest((salt + pin).toByteArray()).toHex()

    private fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }

    private companion object {
        const val KEY_HASH = "parent_pin_hash"
        const val KEY_SALT = "parent_pin_salt"
        const val SALT_SIZE = 16
    }
}
