package com.babatunde.okido.infra

import android.accessibilityservice.AccessibilityService
import android.provider.Settings
import android.view.accessibility.AccessibilityEvent
import com.babatunde.okido.core.AppAccessStore
import com.babatunde.okido.core.Clock
import com.babatunde.okido.core.ScreenLocker
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OkidoAccessibilityService : AccessibilityService(), KoinComponent {
    private val appAccessStore: AppAccessStore by inject()
    private val clock: Clock by inject()
    private val screenLocker: ScreenLocker by inject()

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        val packageName = event.packageName?.toString() ?: return
        if (packageName in alwaysAllowedPackages()) return
        if (!appAccessStore.canUse(packageName, clock.now())) screenLocker.lock()
    }

    override fun onInterrupt() = Unit

    private fun alwaysAllowedPackages(): Set<String> {
        val keyboard = Settings.Secure.getString(contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD)
            ?.substringBefore('/')
        return setOfNotNull(this.packageName, SYSTEM_UI_PACKAGE, keyboard)
    }

    private companion object {
        const val SYSTEM_UI_PACKAGE = "com.android.systemui"
    }
}
