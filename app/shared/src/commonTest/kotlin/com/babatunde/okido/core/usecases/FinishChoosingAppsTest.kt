package com.babatunde.okido.core.usecases

import com.babatunde.okido.core.FakeOnboardingStore
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
