package com.babatunde.okido.core.usecases

import com.babatunde.okido.core.FakeBlockingService
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsBlockingEnabledTest {

    @Test
    fun tellsWhenBlockingIsOff() {
        val isBlockingEnabled = IsBlockingEnabled(FakeBlockingService(enabled = false))

        assertFalse(isBlockingEnabled())
    }

    @Test
    fun tellsWhenBlockingIsOn() {
        val isBlockingEnabled = IsBlockingEnabled(FakeBlockingService(enabled = true))

        assertTrue(isBlockingEnabled())
    }
}
