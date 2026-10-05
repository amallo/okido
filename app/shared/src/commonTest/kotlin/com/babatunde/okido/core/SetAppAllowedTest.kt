package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals

class SetAppAllowedTest {

    private val youtube = LaunchableApp(id = "com.google.android.youtube", label = "YouTube")

    @Test
    fun allowingAddsApp() {
        val store = FakeAllowedAppsStore()
        val setAppAllowed = SetAppAllowed(store)

        setAppAllowed(youtube, allowed = true)

        assertEquals(setOf(youtube.id), store.allowedAppIds())
    }

    @Test
    fun disallowingRemovesApp() {
        val store = FakeAllowedAppsStore(setOf(youtube.id))
        val setAppAllowed = SetAppAllowed(store)

        setAppAllowed(youtube, allowed = false)

        assertEquals(emptySet(), store.allowedAppIds())
    }
}
