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

class FakeAppCatalog(
    private val apps: List<LaunchableApp> = emptyList(),
    private val icons: Map<String, ByteArray> = emptyMap(),
) : AppCatalog {
    val launched = mutableListOf<LaunchableApp>()
    override fun launchableApps(): List<LaunchableApp> = apps
    override fun launch(app: LaunchableApp) {
        launched += app
    }
    override fun icon(app: LaunchableApp): ByteArray? = icons[app.id]
}

class FakePinVerifier(private val validPin: String) : PinVerifier {
    override fun verify(pin: String): Boolean = pin == validPin
}

class FakeAllowedAppsStore(
    allowed: Set<String> = emptySet(),
    private val unlockedUntil: Instant? = null,
) : AllowedAppsStore {
    private val allowed = allowed.toMutableSet()
    override fun allowedAppIds(): Set<String> = allowed.toSet()
    override fun allow(id: String) {
        allowed += id
    }
    override fun disallow(id: String) {
        allowed -= id
    }
    override fun canUse(appId: String, now: Instant): Boolean {
        val unlockedUntil = unlockedUntil ?: return false
        return appId in allowed && now < unlockedUntil
    }
}
