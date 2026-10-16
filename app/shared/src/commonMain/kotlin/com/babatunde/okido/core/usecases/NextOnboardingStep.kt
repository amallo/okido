package com.babatunde.okido.core.usecases

import com.babatunde.okido.core.OnboardingStep
import com.babatunde.okido.core.providers.BlockingService
import com.babatunde.okido.core.providers.HomeApp
import com.babatunde.okido.core.providers.OnboardingStore
import com.babatunde.okido.core.providers.ParentPin

class NextOnboardingStep(
    private val onboardingStore: OnboardingStore,
    private val parentPin: ParentPin,
    private val blockingService: BlockingService,
    private val homeApp: HomeApp,
) {
    operator fun invoke(): OnboardingStep {
        if (onboardingStore.isCompleted()) return OnboardingStep.Done
        if (!parentPin.isSet()) return OnboardingStep.Introduction
        if (!onboardingStore.areAppsChosen()) return OnboardingStep.ChooseApps
        if (!blockingService.isEnabled()) return OnboardingStep.EnableBlocking
        if (!onboardingStore.isBlockingWarningRead()) return OnboardingStep.BlockingCanStop
        if (!homeApp.isOkido()) return OnboardingStep.SetAsHome
        onboardingStore.complete()
        return OnboardingStep.Done
    }
}
