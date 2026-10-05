package com.babatunde.okido.infra

import android.content.SharedPreferences
import com.babatunde.okido.core.OnboardingStore

class SharedPreferencesOnboardingStore(private val preferences: SharedPreferences) : OnboardingStore {
    override fun isCompleted(): Boolean = preferences.getBoolean(KEY_COMPLETED, false)

    override fun complete() {
        preferences.edit().putBoolean(KEY_COMPLETED, true).apply()
    }

    private companion object {
        const val KEY_COMPLETED = "onboarding_completed"
    }
}
