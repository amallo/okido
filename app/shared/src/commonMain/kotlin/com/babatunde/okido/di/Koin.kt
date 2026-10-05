package com.babatunde.okido.di

import com.babatunde.okido.core.CanUseApps
import com.babatunde.okido.core.EndExtraTime
import com.babatunde.okido.core.GrantExtraTime
import com.babatunde.okido.core.LaunchApp
import com.babatunde.okido.infra.infraModule
import com.babatunde.okido.ui.uiModule
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val coreModule = module {
    factoryOf(::GrantExtraTime)
    factoryOf(::EndExtraTime)
    factoryOf(::CanUseApps)
    factoryOf(::LaunchApp)
}

fun initKoin(config: KoinAppDeclaration) {
    startKoin {
        config()
        modules(coreModule, infraModule, uiModule)
    }
}

fun initKoin() = initKoin {}
