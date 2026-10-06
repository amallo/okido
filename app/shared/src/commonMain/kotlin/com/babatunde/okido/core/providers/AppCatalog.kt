package com.babatunde.okido.core.providers

import com.babatunde.okido.core.LaunchableApp

interface AppCatalog {
    fun launchableApps(): List<LaunchableApp>
    fun launch(app: LaunchableApp)
}
