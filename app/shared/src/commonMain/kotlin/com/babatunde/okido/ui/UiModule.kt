package com.babatunde.okido.ui

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/** Koin property set by debug builds to unlock testing shortcuts. */
const val DEBUG_PROPERTY = "debug"

val uiModule = module {
    viewModel { ExtraTimeViewModel(get(), get(), grantableDurations(debug = getProperty(DEBUG_PROPERTY, false))) }
    viewModelOf(::HomeViewModel)
    viewModelOf(::ParentSettingsViewModel)
    viewModelOf(::OnboardingViewModel)
}
