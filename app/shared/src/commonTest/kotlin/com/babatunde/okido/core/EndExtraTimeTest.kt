package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals

class EndExtraTimeTest {

    private class FakeScreenLocker : ScreenLocker {
        var lockCount = 0
        override fun lock() {
            lockCount++
        }
    }

    @Test
    fun locksScreenWhenExtraTimeEnds() {
        val screenLocker = FakeScreenLocker()
        val endExtraTime = EndExtraTime(screenLocker)

        endExtraTime()

        assertEquals(1, screenLocker.lockCount)
    }
}
