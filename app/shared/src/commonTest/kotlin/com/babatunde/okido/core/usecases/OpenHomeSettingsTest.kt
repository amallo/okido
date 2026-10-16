package com.babatunde.okido.core.usecases

import com.babatunde.okido.core.FakeHomeApp
import kotlin.test.Test
import kotlin.test.assertTrue

class OpenHomeSettingsTest {

    @Test
    fun opensTheHomeAppSettings() {
        val homeApp = FakeHomeApp()
        val openHomeSettings = OpenHomeSettings(homeApp)

        openHomeSettings()

        assertTrue(homeApp.settingsOpened)
    }
}
