package com.babatunde.okido.infra

import android.content.Context
import android.content.Intent
import com.babatunde.okido.core.AppCatalog
import com.babatunde.okido.core.LaunchableApp

class PackageManagerAppCatalog(private val context: Context) : AppCatalog {
    override fun launchableApps(): List<LaunchableApp> {
        val packageManager = context.packageManager
        val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        return packageManager.queryIntentActivities(launcherIntent, 0)
            .map { it.activityInfo }
            .filter { it.packageName != context.packageName }
            .distinctBy { it.packageName }
            .map { LaunchableApp(id = it.packageName, label = it.loadLabel(packageManager).toString()) }
            .sortedBy { it.label.lowercase() }
    }

    override fun launch(app: LaunchableApp) {
        val intent = context.packageManager.getLaunchIntentForPackage(app.id) ?: return
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}
