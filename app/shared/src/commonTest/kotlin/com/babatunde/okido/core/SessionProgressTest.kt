package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class SessionProgressTest {

    private val now = Instant.parse("2026-10-05T18:00:00Z")

    @Test
    fun isTheShareOfTheSessionAlreadyUsed() {
        val accessStore = FakeAccessStore(unlockedUntil = now + 10.minutes, sessionStart = now - 30.minutes)
        val sessionProgress = SessionProgress(accessStore, FakeClock(now))

        assertEquals(0.75, sessionProgress())
    }

    @Test
    fun isCompleteWhenNothingWasGranted() {
        val sessionProgress = SessionProgress(FakeAccessStore(), FakeClock(now))

        assertEquals(1.0, sessionProgress())
    }

    @Test
    fun staysCompleteOnceTheGrantedTimeHasEnded() {
        val accessStore = FakeAccessStore(unlockedUntil = now - 5.minutes, sessionStart = now - 20.minutes)
        val sessionProgress = SessionProgress(accessStore, FakeClock(now))

        assertEquals(1.0, sessionProgress())
    }

    @Test
    fun hasNotStartedWhenTheSessionStartIsUnknown() {
        val sessionProgress = SessionProgress(FakeAccessStore(unlockedUntil = now + 10.minutes), FakeClock(now))

        assertEquals(0.0, sessionProgress())
    }
}
