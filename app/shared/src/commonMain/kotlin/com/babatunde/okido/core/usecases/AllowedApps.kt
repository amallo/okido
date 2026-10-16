package com.babatunde.okido.core.usecases

import com.babatunde.okido.core.AppAccess
import com.babatunde.okido.core.LaunchableApp
import com.babatunde.okido.core.providers.AppAccessStore
import com.babatunde.okido.core.providers.AppCatalog

class AllowedApps(
    private val appCatalog: AppCatalog,
    private val appAccessStore: AppAccessStore,
) {
    operator fun invoke(): List<LaunchableApp> {
        val accesses = appAccessStore.accesses()
        return appCatalog.launchableApps().filter { (accesses[it.id] ?: AppAccess.Blocked) != AppAccess.Blocked }
    }
}
