package com.babatunde.okido.infra

import android.content.SharedPreferences
import com.babatunde.okido.core.PinVerifier

// TODO: hash the PIN once the parent can set it from the app.
class SharedPreferencesPinVerifier(private val preferences: SharedPreferences) : PinVerifier {
    override fun verify(pin: String): Boolean =
        pin == preferences.getString(KEY_PARENT_PIN, DEFAULT_PIN)

    private companion object {
        const val KEY_PARENT_PIN = "parent_pin"
        const val DEFAULT_PIN = "1234"
    }
}
