package com.babatunde.okido.infra

import android.content.Context
import android.content.Intent
import com.babatunde.okido.core.ScreenLocker

/**
 * Brings Okido itself to the front rather than the HOME intent: when Okido is not the default home app,
 * HOME would open the system launcher, which is blocked too, and loop.
 */
class HomeScreenLocker(private val context: Context) : ScreenLocker {
    override fun lock() {
        val okido = context.packageManager.getLaunchIntentForPackage(context.packageName) ?: return
        context.startActivity(okido.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}
