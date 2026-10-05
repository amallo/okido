package com.babatunde.okido.ui

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::ExtraTimeViewModel)
    viewModelOf(::HomeViewModel)
}
