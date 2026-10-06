package com.babatunde.okido.infra.providers

import android.content.SharedPreferences
import com.babatunde.okido.core.providers.AccessStore
import kotlin.time.Instant

class SharedPreferencesAccessStore(private val preferences: SharedPreferences) : AccessStore {
    override fun unlockedUntil(): Instant? =
        if (preferences.contains(KEY_UNLOCKED_UNTIL)) {
            Instant.fromEpochMilliseconds(preferences.getLong(KEY_UNLOCKED_UNTIL, 0L))
        } else {
            null
        }

    override fun unlockUntil(instant: Instant) {
        preferences.edit().putLong(KEY_UNLOCKED_UNTIL, instant.toEpochMilliseconds()).apply()
    }

    private companion object {
        const val KEY_UNLOCKED_UNTIL = "unlocked_until_millis"
    }
}
