package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class GrantExtraTimeTest {

    private val now = Instant.parse("2026-10-05T18:00:00Z")

    @Test
    fun unlocksUntilNowPlusDurationWhenPinIsValid() {
        val accessStore = FakeAccessStore()
        val grantExtraTime = GrantExtraTime(FakeParentPin("1234"), accessStore, FakeClock(now))

        val result = grantExtraTime(pin = "1234", duration = 15.minutes)

        assertEquals(GrantExtraTimeResult.Granted, result)
        assertEquals(now + 15.minutes, accessStore.unlockedUntil())
    }

    @Test
    fun addsToRemainingTimeWhenStillUnlocked() {
        val accessStore = FakeAccessStore(unlockedUntil = now + 5.minutes)
        val grantExtraTime = GrantExtraTime(FakeParentPin("1234"), accessStore, FakeClock(now))

        grantExtraTime(pin = "1234", duration = 15.minutes)

        assertEquals(now + 20.minutes, accessStore.unlockedUntil())
    }

    @Test
    fun startsASessionWhenLocked() {
        val accessStore = FakeAccessStore()
        val grantExtraTime = GrantExtraTime(FakeParentPin("1234"), accessStore, FakeClock(now))

        grantExtraTime(pin = "1234", duration = 15.minutes)

        assertEquals(now, accessStore.sessionStart())
    }

    @Test
    fun keepsTheSessionStartWhenStillUnlocked() {
        val accessStore = FakeAccessStore(unlockedUntil = now + 5.minutes, sessionStart = now - 10.minutes)
        val grantExtraTime = GrantExtraTime(FakeParentPin("1234"), accessStore, FakeClock(now))

        grantExtraTime(pin = "1234", duration = 15.minutes)

        assertEquals(now - 10.minutes, accessStore.sessionStart())
    }

    @Test
    fun grantsNothingWhenPinIsInvalid() {
        val accessStore = FakeAccessStore()
        val grantExtraTime = GrantExtraTime(FakeParentPin("1234"), accessStore, FakeClock(now))

        val result = grantExtraTime(pin = "0000", duration = 15.minutes)

        assertEquals(GrantExtraTimeResult.InvalidPin, result)
        assertNull(accessStore.unlockedUntil())
    }
}
