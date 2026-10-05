package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals

class AlwaysAllowedAppsTest {

    private val youtube = LaunchableApp(id = "com.google.android.youtube", label = "YouTube")
    private val clock = LaunchableApp(id = "com.android.deskclock", label = "Horloge")
    private val radio = LaunchableApp(id = "fr.radiofrance.app", label = "Radio")

    @Test
    fun listsOnlyAlwaysAllowedApps() {
        val accesses = mapOf(youtube.id to AppAccess.Timed, radio.id to AppAccess.Always, clock.id to AppAccess.Blocked)
        val appCatalog = FakeAppCatalog(listOf(clock, youtube, radio))
        val alwaysAllowedApps = AlwaysAllowedApps(appCatalog, FakeAppAccessStore(accesses))

        assertEquals(listOf(radio), alwaysAllowedApps())
    }
}
