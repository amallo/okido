package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals

class AllowedAppsTest {

    private val youtube = LaunchableApp(id = "com.google.android.youtube", label = "YouTube")
    private val clock = LaunchableApp(id = "com.android.deskclock", label = "Horloge")

    @Test
    fun listsNoAppByDefault() {
        val allowedApps = AllowedApps(FakeAppCatalog(listOf(clock, youtube)), FakeAppAccessStore())

        assertEquals(emptyList(), allowedApps())
    }

    @Test
    fun listsOnlyAllowedApps() {
        val appAccessStore = FakeAppAccessStore(mapOf(youtube.id to AppAccess.Timed))
        val allowedApps = AllowedApps(FakeAppCatalog(listOf(clock, youtube)), appAccessStore)

        assertEquals(listOf(youtube), allowedApps())
    }

    @Test
    fun listsTimedAndAlwaysAllowedApps() {
        val radio = LaunchableApp(id = "fr.radiofrance.app", label = "Radio")
        val accesses = mapOf(youtube.id to AppAccess.Timed, radio.id to AppAccess.Always, clock.id to AppAccess.Blocked)
        val allowedApps = AllowedApps(FakeAppCatalog(listOf(clock, youtube, radio)), FakeAppAccessStore(accesses))

        assertEquals(listOf(youtube, radio), allowedApps())
    }
}
