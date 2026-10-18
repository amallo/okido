package com.babatunde.okido.di

import com.babatunde.okido.core.usecases.AllowedApps
import com.babatunde.okido.core.usecases.AlwaysAllowedApps
import com.babatunde.okido.core.usecases.AppSettings
import com.babatunde.okido.core.usecases.ChangeParentPin
import com.babatunde.okido.core.usecases.CreateParentPin
import com.babatunde.okido.core.usecases.GrantExtraTime
import com.babatunde.okido.core.usecases.GetAppVersion
import com.babatunde.okido.core.usecases.IsBlockingEnabled
import com.babatunde.okido.core.usecases.LaunchApp
import com.babatunde.okido.core.usecases.LockNow
import com.babatunde.okido.core.usecases.FinishChoosingApps
import com.babatunde.okido.core.usecases.NextOnboardingStep
import com.babatunde.okido.core.usecases.OpenBlockingSettings
import com.babatunde.okido.core.usecases.OpenHomeSettings
import com.babatunde.okido.core.usecases.ReadBlockingWarning
import com.babatunde.okido.core.usecases.RemainingTime
import com.babatunde.okido.core.usecases.SessionProgress
import com.babatunde.okido.core.usecases.SetAppAccess
import com.babatunde.okido.core.usecases.VerifyParentPin
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
    factoryOf(::GetAppVersion)
    factoryOf(::ReadBlockingWarning)
}

fun initKoin(config: KoinAppDeclaration) {
    startKoin {
        config()
        modules(coreModule, infraModule, uiModule)
    }
}

fun initKoin() = initKoin {}
