package com.babatunde.okido.di

import com.babatunde.okido.core.AllowedApps
import com.babatunde.okido.core.AlwaysAllowedApps
import com.babatunde.okido.core.AppIcons
import com.babatunde.okido.core.AppSettings
import com.babatunde.okido.core.EndExtraTime
import com.babatunde.okido.core.GrantExtraTime
import com.babatunde.okido.core.LaunchApp
import com.babatunde.okido.core.LockNow
import com.babatunde.okido.core.RemainingTime
import com.babatunde.okido.core.SetAppAccess
import com.babatunde.okido.core.UnlockParentSettings
import com.babatunde.okido.infra.infraModule
import com.babatunde.okido.ui.uiModule
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val coreModule = module {
    factoryOf(::GrantExtraTime)
    factoryOf(::AppIcons)
    factoryOf(::EndExtraTime)
    factoryOf(::LaunchApp)
    factoryOf(::RemainingTime)
    factoryOf(::LockNow)
    factoryOf(::UnlockParentSettings)
    factoryOf(::AppSettings)
    factoryOf(::SetAppAccess)
    factoryOf(::AllowedApps)
    factoryOf(::AlwaysAllowedApps)
}

fun initKoin(config: KoinAppDeclaration) {
    startKoin {
        config()
        modules(coreModule, infraModule, uiModule)
    }
}

fun initKoin() = initKoin {}
