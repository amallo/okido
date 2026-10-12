package com.babatunde.okido.di

import com.babatunde.okido.core.AllowedApps
import com.babatunde.okido.core.AlwaysAllowedApps
import com.babatunde.okido.core.AppSettings
import com.babatunde.okido.core.ChangeParentPin
import com.babatunde.okido.core.CreateParentPin
import com.babatunde.okido.core.GrantExtraTime
import com.babatunde.okido.core.IsBlockingEnabled
import com.babatunde.okido.core.LaunchApp
import com.babatunde.okido.core.LockNow
import com.babatunde.okido.core.FinishChoosingApps
import com.babatunde.okido.core.NextOnboardingStep
import com.babatunde.okido.core.OpenBlockingSettings
import com.babatunde.okido.core.OpenHomeSettings
import com.babatunde.okido.core.ReadBlockingWarning
import com.babatunde.okido.core.RemainingTime
import com.babatunde.okido.core.SessionProgress
import com.babatunde.okido.core.SetAppAccess
import com.babatunde.okido.core.VerifyParentPin
import com.babatunde.okido.infra.infraModule
import com.babatunde.okido.ui.uiModule
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val coreModule = module {
    factoryOf(::GrantExtraTime)
    factoryOf(::LaunchApp)
    factoryOf(::RemainingTime)
    factoryOf(::SessionProgress)
    factoryOf(::LockNow)
    factoryOf(::VerifyParentPin)
    factoryOf(::AppSettings)
    factoryOf(::SetAppAccess)
    factoryOf(::AllowedApps)
    factoryOf(::AlwaysAllowedApps)
    factoryOf(::NextOnboardingStep)
    factoryOf(::FinishChoosingApps)
    factoryOf(::CreateParentPin)
    factoryOf(::ChangeParentPin)
    factoryOf(::OpenBlockingSettings)
    factoryOf(::OpenHomeSettings)
    factoryOf(::IsBlockingEnabled)
    factoryOf(::ReadBlockingWarning)
}

fun initKoin(config: KoinAppDeclaration) {
    startKoin {
        config()
        modules(coreModule, infraModule, uiModule)
    }
}

fun initKoin() = initKoin {}
