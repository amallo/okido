package com.babatunde.okido.core

class AppIcons(private val appCatalog: AppCatalog) {
    operator fun invoke(apps: List<LaunchableApp>): Map<String, ByteArray> =
        apps.mapNotNull { app -> appCatalog.icon(app)?.let { app.id to it } }.toMap()
}
