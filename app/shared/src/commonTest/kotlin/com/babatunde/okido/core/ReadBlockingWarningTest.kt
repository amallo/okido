package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertTrue

class ReadBlockingWarningTest {

    @Test
    fun remembersThatTheWarningWasRead() {
        val onboardingStore = FakeOnboardingStore()
        val readBlockingWarning = ReadBlockingWarning(onboardingStore)

        readBlockingWarning()

        assertTrue(onboardingStore.blockingWarningRead)
    }
}
