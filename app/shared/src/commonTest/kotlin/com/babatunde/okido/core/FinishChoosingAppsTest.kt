package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertTrue

class FinishChoosingAppsTest {

    @Test
    fun remembersThatTheAppsAreChosen() {
        val onboardingStore = FakeOnboardingStore()

        FinishChoosingApps(onboardingStore)()

        assertTrue(onboardingStore.appsChosen)
    }
}
