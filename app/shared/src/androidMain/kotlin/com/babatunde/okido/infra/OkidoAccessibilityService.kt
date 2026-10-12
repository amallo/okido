package com.babatunde.okido.infra

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.accessibility.AccessibilityEvent
import com.babatunde.okido.core.providers.AppAccessStore
import com.babatunde.okido.core.providers.AppCatalog
import com.babatunde.okido.core.providers.Clock
import com.babatunde.okido.core.providers.ScreenLocker
import kotlin.time.Duration.Companion.seconds
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OkidoAccessibilityService : AccessibilityService(), KoinComponent {
    private val appAccessStore: AppAccessStore by inject()
    private val appCatalog: AppCatalog by inject()
    private val clock: Clock by inject()
    private val screenLocker: ScreenLocker by inject()

    private val handler = Handler(Looper.getMainLooper())
    private var foregroundPackage: String? = null

    /** Extra time can run out while the child stays in the same app, so the foreground app is checked again regularly. */
    private val recheck = object : Runnable {
        override fun run() {
            foregroundPackage?.let(::lockIfNotAllowed)
            handler.postDelayed(this, RECHECK_INTERVAL.inWholeMilliseconds)
        }
    }

    override fun onServiceConnected() {
        handler.postDelayed(recheck, RECHECK_INTERVAL.inWholeMilliseconds)
    }

    override fun onDestroy() {
        handler.removeCallbacks(recheck)
        super.onDestroy()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        val packageName = event.packageName?.toString() ?: return
        // Until the parent makes Okido the home app, the home button opens the system launcher: bring Okido back.
        if (packageName != this.packageName && packageName == systemHomePackage()) {
            screenLocker.lock()
            return
        }
        // The keyboard and screens without a launcher icon (system UI, Google sign-in, permission and
        // share dialogs) show over the app in front, which stays the one to check.
        if (packageName != this.packageName && (!appCatalog.isLaunchable(packageName) || packageName == keyboardPackage())) return
        foregroundPackage = packageName
        lockIfNotAllowed(packageName)
    }

    override fun onInterrupt() = Unit

    private fun lockIfNotAllowed(packageName: String) {
        if (packageName == this.packageName) return
        if (!appAccessStore.canUse(packageName, clock.now())) screenLocker.lock()
    }

    /** The home app Android opens on the home button, or null while it would still ask which one to use. */
    private fun systemHomePackage(): String? {
        val home = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
        return packageManager.resolveActivity(home, PackageManager.MATCH_DEFAULT_ONLY)
            ?.activityInfo?.packageName?.takeIf { it != "android" }
    }

    private fun keyboardPackage(): String? =
        Settings.Secure.getString(contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD)?.substringBefore('/')

    private companion object {
        val RECHECK_INTERVAL = 30.seconds
    }
}
