package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class AppAccessTest {
    private val now = Instant.fromEpochMilliseconds(1_000_000)

    @Test
    fun anAlwaysAllowedAppIsOpenWithoutGrantedTime() {
        assertTrue(AppAccess.Always.isOpen(now, unlockedUntil = null))
    }

    @Test
    fun aTimedAppIsOpenOnlyBeforeTheGrantedTimeEnds() {
        assertTrue(AppAccess.Timed.isOpen(now, unlockedUntil = now + 1.minutes))
        assertFalse(AppAccess.Timed.isOpen(now, unlockedUntil = now))
        assertFalse(AppAccess.Timed.isOpen(now, unlockedUntil = null))
    }

    @Test
    fun aBlockedAppIsNeverOpen() {
        assertFalse(AppAccess.Blocked.isOpen(now, unlockedUntil = now + 1.minutes))
    }
}
