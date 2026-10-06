package com.babatunde.okido.infra.providers

import android.content.SharedPreferences
import com.babatunde.okido.core.providers.OnboardingStore

class SharedPreferencesOnboardingStore(private val preferences: SharedPreferences) : OnboardingStore {
    override fun isCompleted(): Boolean = preferences.getBoolean(KEY_COMPLETED, false)

    override fun complete() {
        preferences.edit().putBoolean(KEY_COMPLETED, true).apply()
    }

    private companion object {
        const val KEY_COMPLETED = "onboarding_completed"
    }
}
