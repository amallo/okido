package com.babatunde.okido.infra

import android.content.Context
import com.babatunde.okido.core.AccessStore
import com.babatunde.okido.core.AppCatalog
import com.babatunde.okido.core.Clock
import com.babatunde.okido.core.ExtraTimeScheduler
import com.babatunde.okido.core.PinVerifier
import com.babatunde.okido.core.ScreenLocker
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val infraModule = module {
    single { androidContext().getSharedPreferences("okido", Context.MODE_PRIVATE) }
    single<PinVerifier> { SharedPreferencesPinVerifier(get()) }
    single<Clock> { DeviceClock() }
    single<AccessStore> { SharedPreferencesAccessStore(get()) }
    single<ExtraTimeScheduler> { AlarmManagerExtraTimeScheduler(androidContext()) }
    single<ScreenLocker> { HomeScreenLocker(androidContext()) }
    single<AppCatalog> { PackageManagerAppCatalog(androidContext()) }
}
