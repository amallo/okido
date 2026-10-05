package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class GrantExtraTimeTest {

    private class FakePinVerifier(private val validPin: String) : PinVerifier {
        override fun verify(pin: String): Boolean = pin == validPin
    }

    private class FakeExtraTimeLedger : ExtraTimeLedger {
        val granted = mutableListOf<Duration>()
        override fun add(duration: Duration) {
            granted += duration
        }
    }

    private class FakeExtraTimeScheduler : ExtraTimeScheduler {
        val scheduled = mutableListOf<Duration>()
        override fun scheduleEnd(after: Duration) {
            scheduled += after
        }
    }

    @Test
    fun grantsExtraTimeWhenPinIsValid() {
        val ledger = FakeExtraTimeLedger()
        val grantExtraTime = GrantExtraTime(FakePinVerifier("1234"), ledger, FakeExtraTimeScheduler())

        val result = grantExtraTime(pin = "1234", duration = 15.minutes)

        assertEquals(GrantExtraTimeResult.Granted, result)
        assertEquals(listOf(15.minutes), ledger.granted)
    }

    @Test
    fun schedulesEndOfExtraTimeWhenPinIsValid() {
        val scheduler = FakeExtraTimeScheduler()
        val grantExtraTime = GrantExtraTime(FakePinVerifier("1234"), FakeExtraTimeLedger(), scheduler)

        grantExtraTime(pin = "1234", duration = 15.minutes)

        assertEquals(listOf(15.minutes), scheduler.scheduled)
    }

    @Test
    fun grantsNothingWhenPinIsInvalid() {
        val ledger = FakeExtraTimeLedger()
        val scheduler = FakeExtraTimeScheduler()
        val grantExtraTime = GrantExtraTime(FakePinVerifier("1234"), ledger, scheduler)

        val result = grantExtraTime(pin = "0000", duration = 15.minutes)

        assertEquals(GrantExtraTimeResult.InvalidPin, result)
        assertEquals(emptyList(), ledger.granted)
        assertEquals(emptyList(), scheduler.scheduled)
    }
}
