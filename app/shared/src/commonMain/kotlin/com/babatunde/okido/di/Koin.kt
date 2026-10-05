package com.babatunde.okido.di

import com.babatunde.okido.core.GetGreeting
import com.babatunde.okido.infra.infraModule
import com.babatunde.okido.ui.uiModule
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val coreModule = module {
    factoryOf(::GetGreeting)
}

fun initKoin(config: KoinAppDeclaration) {
    startKoin {
        config()
        modules(coreModule, infraModule, uiModule)
    }
}

fun initKoin() = initKoin {}
