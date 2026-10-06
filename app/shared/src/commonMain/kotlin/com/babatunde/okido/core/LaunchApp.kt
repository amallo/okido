package com.babatunde.okido.core

import com.babatunde.okido.core.providers.AppAccessStore
import com.babatunde.okido.core.providers.AppCatalog
import com.babatunde.okido.core.providers.Clock
import com.babatunde.okido.core.providers.ScreenLocker

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
