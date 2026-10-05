package com.babatunde.okido.infra

import android.content.SharedPreferences
import com.babatunde.okido.core.ExtraTimeLedger
import kotlin.time.Duration

class SharedPreferencesExtraTimeLedger(private val preferences: SharedPreferences) : ExtraTimeLedger {
    override fun add(duration: Duration) {
        val total = preferences.getLong(KEY_GRANTED_MILLIS, 0L) + duration.inWholeMilliseconds
        preferences.edit().putLong(KEY_GRANTED_MILLIS, total).apply()
    }

    private companion object {
        const val KEY_GRANTED_MILLIS = "extra_time_granted_millis"
    }
}
