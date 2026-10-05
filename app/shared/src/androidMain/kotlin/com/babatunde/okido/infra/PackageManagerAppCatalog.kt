package com.babatunde.okido.infra

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import com.babatunde.okido.core.AppCatalog
import com.babatunde.okido.core.LaunchableApp
import java.io.ByteArrayOutputStream

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

    override fun icon(app: LaunchableApp): ByteArray? {
        val drawable = try {
            context.packageManager.getApplicationIcon(app.id)
        } catch (_: PackageManager.NameNotFoundException) {
            return null
        }
        val bitmap = Bitmap.createBitmap(ICON_SIZE_PX, ICON_SIZE_PX, Bitmap.Config.ARGB_8888)
        drawable.setBounds(0, 0, ICON_SIZE_PX, ICON_SIZE_PX)
        drawable.draw(Canvas(bitmap))
        return ByteArrayOutputStream().use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.toByteArray()
        }
    }

    private companion object {
        const val ICON_SIZE_PX = 144
    }
}
