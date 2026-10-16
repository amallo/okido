package com.babatunde.okido.core.usecases

import com.babatunde.okido.core.providers.AccessStore
import com.babatunde.okido.core.providers.Clock
import com.babatunde.okido.core.providers.ScreenLocker

class LockNow(
    private val accessStore: AccessStore,
    private val clock: Clock,
    private val screenLocker: ScreenLocker,
) {
    operator fun invoke() {
        accessStore.unlockUntil(clock.now())
        screenLocker.lock()
    }
}
