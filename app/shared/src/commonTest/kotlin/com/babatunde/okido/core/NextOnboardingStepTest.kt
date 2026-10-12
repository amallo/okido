package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NextOnboardingStepTest {

    @Test
    fun startsWithTheIntroductionWhenNoPinIsSet() {
        val nextOnboardingStep = NextOnboardingStep(FakeOnboardingStore(), FakeParentPin(), FakeBlockingService(), FakeHomeApp())

        assertEquals(OnboardingStep.Introduction, nextOnboardingStep())
    }

    @Test
    fun asksToChooseTheAppsOnceThePinIsSet() {
        val nextOnboardingStep = NextOnboardingStep(FakeOnboardingStore(), FakeParentPin("1234"), FakeBlockingService(), FakeHomeApp())

        assertEquals(OnboardingStep.ChooseApps, nextOnboardingStep())
    }

    @Test
    fun asksToEnableBlockingOnceTheAppsAreChosen() {
        val nextOnboardingStep = NextOnboardingStep(FakeOnboardingStore(appsChosen = true), FakeParentPin("1234"), FakeBlockingService(), FakeHomeApp())

        assertEquals(OnboardingStep.EnableBlocking, nextOnboardingStep())
    }

    @Test
    fun warnsThatBlockingCanBeTurnedOffOnceItIsEnabled() {
        val nextOnboardingStep = NextOnboardingStep(
            FakeOnboardingStore(appsChosen = true), FakeParentPin("1234"), FakeBlockingService(enabled = true), FakeHomeApp(),
        )

        assertEquals(OnboardingStep.BlockingCanStop, nextOnboardingStep())
    }

    @Test
    fun asksToMakeOkidoTheHomeScreenOnceTheWarningIsRead() {
        val nextOnboardingStep = NextOnboardingStep(
            FakeOnboardingStore(appsChosen = true, blockingWarningRead = true), FakeParentPin("1234"), FakeBlockingService(enabled = true), FakeHomeApp(),
        )

        assertEquals(OnboardingStep.SetAsHome, nextOnboardingStep())
    }

    @Test
    fun completesTheOnboardingOnceOkidoIsTheHomeScreen() {
        val onboardingStore = FakeOnboardingStore(appsChosen = true, blockingWarningRead = true)
        val nextOnboardingStep = NextOnboardingStep(onboardingStore, FakeParentPin("1234"), FakeBlockingService(enabled = true), FakeHomeApp(okido = true))

        assertEquals(OnboardingStep.Done, nextOnboardingStep())
        assertTrue(onboardingStore.completed)
    }

    @Test
    fun staysDoneOnceCompletedEvenIfBlockingIsDisabled() {
        val onboardingStore = FakeOnboardingStore(completed = true)
        val nextOnboardingStep = NextOnboardingStep(onboardingStore, FakeParentPin("1234"), FakeBlockingService(enabled = false), FakeHomeApp())

        assertEquals(OnboardingStep.Done, nextOnboardingStep())
    }
}
