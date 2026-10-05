package com.babatunde.okido.core

class LaunchApp(
    private val appAccessStore: AppAccessStore,
    private val clock: Clock,
    private val appCatalog: AppCatalog,
    private val screenLocker: ScreenLocker,
) {
    operator fun invoke(app: LaunchableApp) {
        if (appAccessStore.canUse(app.id, clock.now())) {
            appCatalog.launch(app)
        } else {
            screenLocker.lock()
        }
    }
}
