package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CreateParentPinTest {

    @Test
    fun savesTheConfirmedPin() {
        val parentPin = FakeParentPin()
        val createParentPin = CreateParentPin(parentPin)

        assertEquals(CreateParentPinResult.Created, createParentPin(pin = "2580", confirmation = "2580"))
        assertTrue(parentPin.verify("2580"))
    }

    @Test
    fun savesNothingWhenTheConfirmationDiffers() {
        val parentPin = FakeParentPin()
        val createParentPin = CreateParentPin(parentPin)

        assertEquals(CreateParentPinResult.Mismatch, createParentPin(pin = "2580", confirmation = "2581"))
        assertFalse(parentPin.isSet())
    }

    @Test
    fun keepsTheExistingPin() {
        val parentPin = FakeParentPin("1234")
        val createParentPin = CreateParentPin(parentPin)

        assertEquals(CreateParentPinResult.AlreadySet, createParentPin(pin = "2580", confirmation = "2580"))
        assertTrue(parentPin.verify("1234"))
    }
}
