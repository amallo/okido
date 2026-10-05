package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertTrue

class OpenBlockingSettingsTest {

    @Test
    fun opensTheBlockingServiceSettings() {
        val blockingService = FakeBlockingService()
        val openBlockingSettings = OpenBlockingSettings(blockingService)

        openBlockingSettings()

        assertTrue(blockingService.settingsOpened)
    }
}
