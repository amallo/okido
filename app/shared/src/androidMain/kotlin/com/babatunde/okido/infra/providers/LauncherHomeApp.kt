package com.babatunde.okido.infra.providers

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import com.babatunde.okido.core.providers.HomeApp

/** Okido is the home app when Android resolves the home button to it, which only the parent can choose in settings. */
class LauncherHomeApp(private val context: Context) : HomeApp {
    override fun isOkido(): Boolean {
        val home = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
        val resolved = context.packageManager.resolveActivity(home, PackageManager.MATCH_DEFAULT_ONLY)
        return resolved?.activityInfo?.packageName == context.packageName
    }

    override fun openSettings() {
        val settings = Intent(Settings.ACTION_HOME_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(settings)
    }
}
