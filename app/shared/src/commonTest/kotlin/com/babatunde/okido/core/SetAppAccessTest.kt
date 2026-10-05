package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals

class SetAppAccessTest {

    private val youtube = LaunchableApp(id = "com.google.android.youtube", label = "YouTube")

    @Test
    fun storesTheChosenAccess() {
        val store = FakeAppAccessStore(mapOf(youtube.id to AppAccess.Timed))
        val setAppAccess = SetAppAccess(store)

        setAppAccess(youtube, AppAccess.Always)

        assertEquals(mapOf(youtube.id to AppAccess.Always), store.accesses())
    }
}
