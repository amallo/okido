package com.babatunde.okido.core.providers

import com.babatunde.okido.core.LaunchableApp

interface AppCatalog {
    fun launchableApps(): List<LaunchableApp>
    fun launch(app: LaunchableApp)

    /** Whether [appId] is an app the child can open from a launcher, as opposed to a system screen or dialog. */
    fun isLaunchable(appId: String): Boolean
}
