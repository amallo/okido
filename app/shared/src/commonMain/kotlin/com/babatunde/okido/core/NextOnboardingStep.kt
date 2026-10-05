package com.babatunde.okido.core

class NextOnboardingStep(
    private val onboardingStore: OnboardingStore,
    private val parentPin: ParentPin,
    private val blockingService: BlockingService,
) {
    operator fun invoke(): OnboardingStep {
        if (onboardingStore.isCompleted()) return OnboardingStep.Done
        if (!parentPin.isSet()) return OnboardingStep.Introduction
        if (!blockingService.isEnabled()) return OnboardingStep.EnableBlocking
        onboardingStore.complete()
        return OnboardingStep.Done
    }
}
