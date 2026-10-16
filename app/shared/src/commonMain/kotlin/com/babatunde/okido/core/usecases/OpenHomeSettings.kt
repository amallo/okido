package com.babatunde.okido.core.usecases

import com.babatunde.okido.core.providers.HomeApp

class OpenHomeSettings(private val homeApp: HomeApp) {
    operator fun invoke() = homeApp.openSettings()
}
