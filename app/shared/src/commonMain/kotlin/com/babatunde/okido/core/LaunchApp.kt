package com.babatunde.okido.core

class LaunchApp(
    private val allowedAppsStore: AllowedAppsStore,
    private val clock: Clock,
    private val appCatalog: AppCatalog,
    private val screenLocker: ScreenLocker,
) {
    operator fun invoke(app: LaunchableApp) {
        if (allowedAppsStore.canUse(app.id, clock.now())) {
            appCatalog.launch(app)
        } else {
            screenLocker.lock()
        }
    }
}
