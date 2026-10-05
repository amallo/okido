package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals

class GetGreetingTest {

    private class FakeDeviceInfo(override val name: String) : DeviceInfo

    @Test
    fun greetsTheDevice() {
        val getGreeting = GetGreeting(FakeDeviceInfo("Test 1"))

        assertEquals("Hello, Test 1!", getGreeting())
    }
}
