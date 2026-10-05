package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class CanUseAppsTest {

    private val now = Instant.parse("2026-10-05T18:00:00Z")

    @Test
    fun cannotUseAppsWhenNoTimeWasGranted() {
        val canUseApps = CanUseApps(FakeAccessStore(), FakeClock(now))

        assertFalse(canUseApps())
    }

    @Test
    fun canUseAppsBeforeEndOfGrantedTime() {
        val canUseApps = CanUseApps(FakeAccessStore(unlockedUntil = now + 1.minutes), FakeClock(now))

        assertTrue(canUseApps())
    }

    @Test
    fun cannotUseAppsOnceGrantedTimeHasEnded() {
        val canUseApps = CanUseApps(FakeAccessStore(unlockedUntil = now), FakeClock(now))

        assertFalse(canUseApps())
    }
}
