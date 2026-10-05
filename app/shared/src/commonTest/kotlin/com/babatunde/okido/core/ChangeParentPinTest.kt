package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ChangeParentPinTest {

    @Test
    fun replacesThePinWhenTheCurrentOneIsValid() {
        val parentPin = FakeParentPin("1234")
        val changeParentPin = ChangeParentPin(parentPin)

        assertEquals(ChangeParentPinResult.Changed, changeParentPin(current = "1234", new = "2580", confirmation = "2580"))
        assertTrue(parentPin.verify("2580"))
        assertFalse(parentPin.verify("1234"))
    }

    @Test
    fun keepsThePinWhenTheCurrentOneIsInvalid() {
        val parentPin = FakeParentPin("1234")
        val changeParentPin = ChangeParentPin(parentPin)

        assertEquals(ChangeParentPinResult.InvalidPin, changeParentPin(current = "0000", new = "2580", confirmation = "2580"))
        assertTrue(parentPin.verify("1234"))
    }

    @Test
    fun keepsThePinWhenTheConfirmationDiffers() {
        val parentPin = FakeParentPin("1234")
        val changeParentPin = ChangeParentPin(parentPin)

        assertEquals(ChangeParentPinResult.Mismatch, changeParentPin(current = "1234", new = "2580", confirmation = "2581"))
        assertTrue(parentPin.verify("1234"))
    }
}
