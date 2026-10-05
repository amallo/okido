package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals

class EndExtraTimeTest {

    @Test
    fun locksScreenWhenExtraTimeEnds() {
        val screenLocker = FakeScreenLocker()
        val endExtraTime = EndExtraTime(screenLocker)

        endExtraTime()

        assertEquals(1, screenLocker.lockCount)
    }
}
