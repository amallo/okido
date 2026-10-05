package com.babatunde.okido.core

class AlwaysAllowedApps(
    private val appCatalog: AppCatalog,
    private val appAccessStore: AppAccessStore,
) {
    operator fun invoke(): List<LaunchableApp> {
        val accesses = appAccessStore.accesses()
        return appCatalog.launchableApps().filter { accesses[it.id] == AppAccess.Always }
    }
}
