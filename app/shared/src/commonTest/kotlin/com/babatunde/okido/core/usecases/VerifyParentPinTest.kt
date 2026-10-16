package com.babatunde.okido.core.usecases

import com.babatunde.okido.core.FakeParentPin
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VerifyParentPinTest {

    @Test
    fun acceptsTheParentPin() {
        val verifyParentPin = VerifyParentPin(FakeParentPin("1234"))

        assertTrue(verifyParentPin(pin = "1234"))
    }

    @Test
    fun rejectsAnyOtherPin() {
        val verifyParentPin = VerifyParentPin(FakeParentPin("1234"))

        assertFalse(verifyParentPin(pin = "0000"))
    }
}
