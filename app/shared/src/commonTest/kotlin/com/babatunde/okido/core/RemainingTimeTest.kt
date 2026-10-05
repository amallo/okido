package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class RemainingTimeTest {

    private val now = Instant.parse("2026-10-05T18:00:00Z")

    @Test
    fun noTimeRemainsWhenNothingWasGranted() {
        val remainingTime = RemainingTime(FakeAccessStore(), FakeClock(now))

        assertEquals(Duration.ZERO, remainingTime())
    }

    @Test
    fun remainingTimeIsTimeUntilEndOfGrantedTime() {
        val remainingTime = RemainingTime(FakeAccessStore(unlockedUntil = now + 25.minutes), FakeClock(now))

        assertEquals(25.minutes, remainingTime())
    }

    @Test
    fun noTimeRemainsOnceGrantedTimeHasEnded() {
        val remainingTime = RemainingTime(FakeAccessStore(unlockedUntil = now - 5.minutes), FakeClock(now))

        assertEquals(Duration.ZERO, remainingTime())
    }
}
