package com.babatunde.okido.core.usecases

import com.babatunde.okido.core.AppAccess
import com.babatunde.okido.core.AppSetting
import com.babatunde.okido.core.FakeAppAccessStore
import com.babatunde.okido.core.FakeAppCatalog
import com.babatunde.okido.core.LaunchableApp
import kotlin.test.Test
import kotlin.test.assertEquals

class AppSettingsTest {

    private val youtube = LaunchableApp(id = "com.google.android.youtube", label = "YouTube")
    private val clock = LaunchableApp(id = "com.android.deskclock", label = "Horloge")

    @Test
    fun allowsNoAppByDefault() {
        val appSettings = AppSettings(FakeAppCatalog(listOf(clock, youtube)), FakeAppAccessStore())

        assertEquals(
            listOf(AppSetting(clock, AppAccess.Blocked), AppSetting(youtube, AppAccess.Blocked)),
            appSettings(),
        )
    }

    @Test
    fun givesEachAppItsAccess() {
        val appAccessStore = FakeAppAccessStore(mapOf(clock.id to AppAccess.Always, youtube.id to AppAccess.Timed))
        val appSettings = AppSettings(FakeAppCatalog(listOf(clock, youtube)), appAccessStore)

        assertEquals(
            listOf(AppSetting(clock, AppAccess.Always), AppSetting(youtube, AppAccess.Timed)),
            appSettings(),
        )
    }
}
