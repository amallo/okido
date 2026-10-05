package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UnlockParentSettingsTest {

    @Test
    fun unlocksWhenPinIsValid() {
        val unlockParentSettings = UnlockParentSettings(FakePinVerifier("1234"))

        assertTrue(unlockParentSettings(pin = "1234"))
    }

    @Test
    fun staysLockedWhenPinIsInvalid() {
        val unlockParentSettings = UnlockParentSettings(FakePinVerifier("1234"))

        assertFalse(unlockParentSettings(pin = "0000"))
    }
}
