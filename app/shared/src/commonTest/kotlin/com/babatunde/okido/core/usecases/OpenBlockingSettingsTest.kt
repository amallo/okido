package com.babatunde.okido.core.usecases

import com.babatunde.okido.core.FakeBlockingService
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
