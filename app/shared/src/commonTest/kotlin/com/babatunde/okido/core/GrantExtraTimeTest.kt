package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class GrantExtraTimeTest {

    private class FakePinVerifier(private val validPin: String) : PinVerifier {
        override fun verify(pin: String): Boolean = pin == validPin
    }

    private class FakeExtraTimeScheduler : ExtraTimeScheduler {
        val scheduled = mutableListOf<Duration>()
        override fun scheduleEnd(after: Duration) {
            scheduled += after
        }
    }

    private val now = Instant.parse("2026-10-05T18:00:00Z")

    @Test
    fun unlocksUntilNowPlusDurationWhenPinIsValid() {
        val accessStore = FakeAccessStore()
        val grantExtraTime = GrantExtraTime(FakePinVerifier("1234"), accessStore, FakeExtraTimeScheduler(), FakeClock(now))

        val result = grantExtraTime(pin = "1234", duration = 15.minutes)

        assertEquals(GrantExtraTimeResult.Granted, result)
        assertEquals(now + 15.minutes, accessStore.unlockedUntil())
    }

    @Test
    fun schedulesEndOfExtraTimeWhenPinIsValid() {
        val scheduler = FakeExtraTimeScheduler()
        val grantExtraTime = GrantExtraTime(FakePinVerifier("1234"), FakeAccessStore(), scheduler, FakeClock(now))

        grantExtraTime(pin = "1234", duration = 15.minutes)

        assertEquals(listOf(15.minutes), scheduler.scheduled)
    }

    @Test
    fun addsToRemainingTimeWhenStillUnlocked() {
        val accessStore = FakeAccessStore(unlockedUntil = now + 5.minutes)
        val scheduler = FakeExtraTimeScheduler()
        val grantExtraTime = GrantExtraTime(FakePinVerifier("1234"), accessStore, scheduler, FakeClock(now))

        grantExtraTime(pin = "1234", duration = 15.minutes)

        assertEquals(now + 20.minutes, accessStore.unlockedUntil())
        assertEquals(listOf(20.minutes), scheduler.scheduled)
    }

    @Test
    fun grantsNothingWhenPinIsInvalid() {
        val accessStore = FakeAccessStore()
        val scheduler = FakeExtraTimeScheduler()
        val grantExtraTime = GrantExtraTime(FakePinVerifier("1234"), accessStore, scheduler, FakeClock(now))

        val result = grantExtraTime(pin = "0000", duration = 15.minutes)

        assertEquals(GrantExtraTimeResult.InvalidPin, result)
        assertNull(accessStore.unlockedUntil())
        assertEquals(emptyList(), scheduler.scheduled)
    }
}
