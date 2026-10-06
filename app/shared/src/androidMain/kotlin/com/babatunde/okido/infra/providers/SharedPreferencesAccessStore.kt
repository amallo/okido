package com.babatunde.okido.infra.providers

import android.content.SharedPreferences
import com.babatunde.okido.core.providers.AccessStore
import kotlin.time.Instant

class SharedPreferencesAccessStore(private val preferences: SharedPreferences) : AccessStore {
    override fun unlockedUntil(): Instant? = instant(KEY_UNLOCKED_UNTIL)

    override fun unlockUntil(instant: Instant) = save(KEY_UNLOCKED_UNTIL, instant)

    override fun sessionStart(): Instant? = instant(KEY_SESSION_START)

    override fun startSession(instant: Instant) = save(KEY_SESSION_START, instant)

    private fun instant(key: String): Instant? =
        if (preferences.contains(key)) Instant.fromEpochMilliseconds(preferences.getLong(key, 0L)) else null

    private fun save(key: String, instant: Instant) {
        preferences.edit().putLong(key, instant.toEpochMilliseconds()).apply()
    }

    private companion object {
        const val KEY_UNLOCKED_UNTIL = "unlocked_until_millis"
        const val KEY_SESSION_START = "session_start_millis"
    }
}
