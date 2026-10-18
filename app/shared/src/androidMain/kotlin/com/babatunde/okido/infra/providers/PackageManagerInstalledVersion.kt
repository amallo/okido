package com.babatunde.okido.infra.providers

import android.content.Context
import android.os.Build
import com.babatunde.okido.core.AppVersion
import com.babatunde.okido.core.providers.InstalledVersion

/** Reads the version Android installed, set at build time from app/version.properties. */
class PackageManagerInstalledVersion(private val context: Context) : InstalledVersion {
    override fun get(): AppVersion {
        val info = context.packageManager.getPackageInfo(context.packageName, 0)
        // longVersionCode only exists from Android 9, and Okido runs from Android 7.
        @Suppress("DEPRECATION")
        val build = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) info.longVersionCode else info.versionCode.toLong()
        return AppVersion(info.versionName.orEmpty(), build)
    }
}
