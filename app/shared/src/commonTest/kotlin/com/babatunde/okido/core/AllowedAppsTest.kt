package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals

class AllowedAppsTest {

    private val youtube = LaunchableApp(id = "com.google.android.youtube", label = "YouTube")
    private val clock = LaunchableApp(id = "com.android.deskclock", label = "Horloge")

    @Test
    fun listsNoAppByDefault() {
        val allowedApps = AllowedApps(FakeAppCatalog(listOf(clock, youtube)), FakeAllowedAppsStore())

        assertEquals(emptyList(), allowedApps())
    }

    @Test
    fun listsOnlyAllowedApps() {
        val allowedApps = AllowedApps(FakeAppCatalog(listOf(clock, youtube)), FakeAllowedAppsStore(setOf(youtube.id)))

        assertEquals(listOf(youtube), allowedApps())
    }
}
