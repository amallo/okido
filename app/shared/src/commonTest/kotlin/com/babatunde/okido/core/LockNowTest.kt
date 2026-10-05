package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class LockNowTest {

    private val now = Instant.parse("2026-10-05T18:00:00Z")

    @Test
    fun endsGrantedTimeImmediately() {
        val accessStore = FakeAccessStore(unlockedUntil = now + 20.minutes)
        val lockNow = LockNow(accessStore, FakeClock(now), FakeScreenLocker())

        lockNow()

        assertEquals(now, accessStore.unlockedUntil())
    }

    @Test
    fun locksScreen() {
        val screenLocker = FakeScreenLocker()
        val lockNow = LockNow(FakeAccessStore(unlockedUntil = now + 20.minutes), FakeClock(now), screenLocker)

        lockNow()

        assertEquals(1, screenLocker.lockCount)
    }
}
