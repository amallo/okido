package com.babatunde.okido.core

interface AppCatalog {
    fun launchableApps(): List<LaunchableApp>
    fun launch(app: LaunchableApp)

    /** The app's icon as PNG bytes, or null when the platform has none. */
    fun icon(app: LaunchableApp): ByteArray?
}
