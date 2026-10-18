package com.babatunde.okido.infra

import android.content.Context
import com.babatunde.okido.core.providers.AccessStore
import com.babatunde.okido.core.providers.AppAccessStore
import com.babatunde.okido.core.providers.AppCatalog
import com.babatunde.okido.core.providers.AppIconLoader
import com.babatunde.okido.core.providers.BlockingService
import com.babatunde.okido.core.providers.Clock
import com.babatunde.okido.core.providers.HomeApp
import com.babatunde.okido.core.providers.InstalledVersion
import com.babatunde.okido.core.providers.OnboardingStore
import com.babatunde.okido.core.providers.ParentPin
import com.babatunde.okido.core.providers.ScreenLocker
import com.babatunde.okido.infra.providers.AccessibilityBlockingService
import com.babatunde.okido.infra.providers.DeviceClock
import com.babatunde.okido.infra.providers.HomeScreenLocker
import com.babatunde.okido.infra.providers.LauncherHomeApp
import com.babatunde.okido.infra.providers.PackageManagerInstalledVersion
import com.babatunde.okido.infra.providers.PackageManagerAppIconLoader
import com.babatunde.okido.infra.providers.PackageManagerAppCatalog
import com.babatunde.okido.infra.providers.SharedPreferencesAccessStore
import com.babatunde.okido.infra.providers.SharedPreferencesAppAccessStore
import com.babatunde.okido.infra.providers.SharedPreferencesOnboardingStore
import com.babatunde.okido.infra.providers.SharedPreferencesParentPin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val infraModule = module {
    single { androidContext().getSharedPreferences("okido", Context.MODE_PRIVATE) }
    single<ParentPin> { SharedPreferencesParentPin(get()) }
    single<Clock> { DeviceClock() }
    single<AccessStore> { SharedPreferencesAccessStore(get()) }
    single<AppAccessStore> { SharedPreferencesAppAccessStore(get(), get()) }
    single<ScreenLocker> { HomeScreenLocker(androidContext()) }
    single<AppCatalog> { PackageManagerAppCatalog(androidContext()) }
    single<AppIconLoader> { PackageManagerAppIconLoader(androidContext()) }
    single<BlockingService> { AccessibilityBlockingService(androidContext()) }
    single<HomeApp> { LauncherHomeApp(androidContext()) }
    single<InstalledVersion> { PackageManagerInstalledVersion(androidContext()) }
    single<OnboardingStore> { SharedPreferencesOnboardingStore(get()) }
}
