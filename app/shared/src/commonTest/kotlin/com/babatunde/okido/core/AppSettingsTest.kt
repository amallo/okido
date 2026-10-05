package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals

class AppSettingsTest {

    private val youtube = LaunchableApp(id = "com.google.android.youtube", label = "YouTube")
    private val clock = LaunchableApp(id = "com.android.deskclock", label = "Horloge")

    @Test
    fun allowsNoAppByDefault() {
        val appSettings = AppSettings(FakeAppCatalog(listOf(clock, youtube)), FakeAllowedAppsStore())

        assertEquals(
            listOf(AppSetting(clock, allowed = false), AppSetting(youtube, allowed = false)),
            appSettings(),
        )
    }

    @Test
    fun marksAllowedApps() {
        val appSettings = AppSettings(FakeAppCatalog(listOf(clock, youtube)), FakeAllowedAppsStore(setOf(clock.id)))

        assertEquals(
            listOf(AppSetting(clock, allowed = true), AppSetting(youtube, allowed = false)),
            appSettings(),
        )
    }
}
