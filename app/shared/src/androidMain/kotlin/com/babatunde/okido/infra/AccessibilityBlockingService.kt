package com.babatunde.okido.infra

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.babatunde.okido.core.BlockingService

/** Blocking relies on [OkidoAccessibilityService], which only the parent can turn on in Android settings. */
class AccessibilityBlockingService(private val context: Context) : BlockingService {
    override fun isEnabled(): Boolean {
        val service = ComponentName(context, OkidoAccessibilityService::class.java)
        val enabled = Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            ?: return false
        return enabled.split(':').any { ComponentName.unflattenFromString(it) == service }
    }

    override fun openSettings() {
        val settings = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(settings)
    }
}
