package com.babatunde.okido.core.usecases

import com.babatunde.okido.core.providers.OnboardingStore

class FinishChoosingApps(private val onboardingStore: OnboardingStore) {
    operator fun invoke() {
        onboardingStore.chooseApps()
    }
}
