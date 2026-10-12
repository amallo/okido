package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NextOnboardingStepTest {

    @Test
    fun startsWithTheIntroductionWhenNoPinIsSet() {
        val nextOnboardingStep = NextOnboardingStep(FakeOnboardingStore(), FakeParentPin(), FakeBlockingService())

        assertEquals(OnboardingStep.Introduction, nextOnboardingStep())
    }

    @Test
    fun asksToChooseTheAppsOnceThePinIsSet() {
        val nextOnboardingStep = NextOnboardingStep(FakeOnboardingStore(), FakeParentPin("1234"), FakeBlockingService())

        assertEquals(OnboardingStep.ChooseApps, nextOnboardingStep())
    }

    @Test
    fun asksToEnableBlockingOnceTheAppsAreChosen() {
        val nextOnboardingStep = NextOnboardingStep(FakeOnboardingStore(appsChosen = true), FakeParentPin("1234"), FakeBlockingService())

        assertEquals(OnboardingStep.EnableBlocking, nextOnboardingStep())
    }

    @Test
    fun completesTheOnboardingOnceBlockingIsEnabled() {
        val onboardingStore = FakeOnboardingStore(appsChosen = true)
        val nextOnboardingStep = NextOnboardingStep(onboardingStore, FakeParentPin("1234"), FakeBlockingService(enabled = true))

        assertEquals(OnboardingStep.Done, nextOnboardingStep())
        assertTrue(onboardingStore.completed)
    }

    @Test
    fun staysDoneOnceCompletedEvenIfBlockingIsDisabled() {
        val onboardingStore = FakeOnboardingStore(completed = true)
        val nextOnboardingStep = NextOnboardingStep(onboardingStore, FakeParentPin("1234"), FakeBlockingService(enabled = false))

        assertEquals(OnboardingStep.Done, nextOnboardingStep())
    }
}
