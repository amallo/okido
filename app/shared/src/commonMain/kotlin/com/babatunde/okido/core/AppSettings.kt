package com.babatunde.okido.core

import com.babatunde.okido.core.providers.AppAccessStore
import com.babatunde.okido.core.providers.AppCatalog

class AppSettings(
    private val appCatalog: AppCatalog,
    private val appAccessStore: AppAccessStore,
) {
    operator fun invoke(): List<AppSetting> {
        val accesses = appAccessStore.accesses()
        return appCatalog.launchableApps().map { AppSetting(it, accesses[it.id] ?: AppAccess.Blocked) }
    }
}
