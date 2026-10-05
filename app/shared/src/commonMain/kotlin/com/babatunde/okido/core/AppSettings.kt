package com.babatunde.okido.core

class AppSettings(
    private val appCatalog: AppCatalog,
    private val allowedAppsStore: AllowedAppsStore,
) {
    operator fun invoke(): List<AppSetting> {
        val allowedIds = allowedAppsStore.allowedAppIds()
        return appCatalog.launchableApps().map { AppSetting(it, allowed = it.id in allowedIds) }
    }
}
