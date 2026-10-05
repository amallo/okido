package com.babatunde.okido.core

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
