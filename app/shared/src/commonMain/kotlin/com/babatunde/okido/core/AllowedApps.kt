package com.babatunde.okido.core

class AllowedApps(
    private val appCatalog: AppCatalog,
    private val allowedAppsStore: AllowedAppsStore,
) {
    operator fun invoke(): List<LaunchableApp> {
        val allowedIds = allowedAppsStore.allowedAppIds()
        return appCatalog.launchableApps().filter { it.id in allowedIds }
    }
}
