package com.babatunde.okido.core

import kotlin.time.Instant

class FakeAccessStore(private var unlockedUntil: Instant? = null) : AccessStore {
    override fun unlockedUntil(): Instant? = unlockedUntil
    override fun unlockUntil(instant: Instant) {
        unlockedUntil = instant
    }
}

class FakeClock(var now: Instant) : Clock {
    override fun now(): Instant = now
}

class FakeScreenLocker : ScreenLocker {
    var lockCount = 0
    override fun lock() {
        lockCount++
    }
}

class FakeAppCatalog(private val apps: List<LaunchableApp> = emptyList()) : AppCatalog {
    val launched = mutableListOf<LaunchableApp>()
    override fun launchableApps(): List<LaunchableApp> = apps
    override fun launch(app: LaunchableApp) {
        launched += app
    }
}
