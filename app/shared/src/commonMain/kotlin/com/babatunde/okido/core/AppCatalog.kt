package com.babatunde.okido.core

interface AppCatalog {
    fun launchableApps(): List<LaunchableApp>
    fun launch(app: LaunchableApp)
}
