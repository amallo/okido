package com.babatunde.okido.core

import com.babatunde.okido.core.providers.AccessStore
import com.babatunde.okido.core.providers.AppAccessStore
import com.babatunde.okido.core.providers.AppCatalog
import com.babatunde.okido.core.providers.BlockingService
import com.babatunde.okido.core.providers.Clock
import com.babatunde.okido.core.providers.OnboardingStore
import com.babatunde.okido.core.providers.ParentPin
import com.babatunde.okido.core.providers.ScreenLocker
import kotlin.time.Instant

class FakeAccessStore(
    private var unlockedUntil: Instant? = null,
    private var sessionStart: Instant? = null,
) : AccessStore {
    override fun unlockedUntil(): Instant? = unlockedUntil
    override fun unlockUntil(instant: Instant) {
        unlockedUntil = instant
    }
    override fun sessionStart(): Instant? = sessionStart
    override fun startSession(instant: Instant) {
        sessionStart = instant
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
) : AppCatalog {
    val launched = mutableListOf<LaunchableApp>()
    override fun launchableApps(): List<LaunchableApp> = apps
    override fun launch(app: LaunchableApp) {
        launched += app
    }
    override fun isLaunchable(appId: String): Boolean = apps.any { it.id == appId }
}

class FakeParentPin(private var pin: String? = null) : ParentPin {
    override fun verify(pin: String): Boolean = pin == this.pin
    override fun isSet(): Boolean = pin != null
    override fun save(pin: String) {
        this.pin = pin
    }
}

class FakeAppAccessStore(
    accesses: Map<String, AppAccess> = emptyMap(),
    private val unlockedUntil: Instant? = null,
) : AppAccessStore {
    private val accesses = accesses.toMutableMap()
    override fun accesses(): Map<String, AppAccess> = accesses.toMap()
    override fun setAccess(appId: String, access: AppAccess) {
        accesses[appId] = access
    }
    override fun canUse(appId: String, now: Instant): Boolean =
        (accesses[appId] ?: AppAccess.Blocked).isOpen(now, unlockedUntil)
}

class FakeOnboardingStore(var completed: Boolean = false, var appsChosen: Boolean = false) : OnboardingStore {
    override fun isCompleted(): Boolean = completed
    override fun areAppsChosen(): Boolean = appsChosen
    override fun chooseApps() {
        appsChosen = true
    }
    override fun complete() {
        completed = true
    }
}

class FakeBlockingService(private val enabled: Boolean = false) : BlockingService {
    var settingsOpened = false
    override fun isEnabled(): Boolean = enabled
    override fun openSettings() {
        settingsOpened = true
    }
}
