package com.babatunde.okido.core

class LaunchApp(
    private val canUseApps: CanUseApps,
    private val appCatalog: AppCatalog,
    private val screenLocker: ScreenLocker,
) {
    operator fun invoke(app: LaunchableApp) {
        if (canUseApps()) {
            appCatalog.launch(app)
        } else {
            screenLocker.lock()
        }
    }
}
