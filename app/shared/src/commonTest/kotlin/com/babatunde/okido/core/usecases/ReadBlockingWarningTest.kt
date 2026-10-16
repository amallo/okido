package com.babatunde.okido.core.usecases

import com.babatunde.okido.core.FakeOnboardingStore
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
