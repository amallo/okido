package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class LaunchAppTest {

    private val now = Instant.parse("2026-10-05T18:00:00Z")
    private val youtube = LaunchableApp(id = "com.google.android.youtube", label = "YouTube")

    @Test
    fun launchesAppWhenTimeRemains() {
        val appCatalog = FakeAppCatalog()
        val appAccessStore = FakeAppAccessStore(mapOf(youtube.id to AppAccess.Timed), unlockedUntil = now + 1.minutes)
        val launchApp = LaunchApp(appAccessStore, FakeClock(now), appCatalog, FakeScreenLocker())

        launchApp(youtube)

        assertEquals(listOf(youtube), appCatalog.launched)
    }

    @Test
    fun locksInsteadOfLaunchingWhenNoTimeRemains() {
        val appCatalog = FakeAppCatalog()
        val screenLocker = FakeScreenLocker()
        val appAccessStore = FakeAppAccessStore(mapOf(youtube.id to AppAccess.Timed), unlockedUntil = now)
        val launchApp = LaunchApp(appAccessStore, FakeClock(now), appCatalog, screenLocker)

        launchApp(youtube)

        assertEquals(emptyList(), appCatalog.launched)
        assertEquals(1, screenLocker.lockCount)
    }

    @Test
    fun locksInsteadOfLaunchingWhenAppIsNotAllowed() {
        val appCatalog = FakeAppCatalog()
        val screenLocker = FakeScreenLocker()
        val appAccessStore = FakeAppAccessStore(unlockedUntil = now + 1.minutes)
        val launchApp = LaunchApp(appAccessStore, FakeClock(now), appCatalog, screenLocker)

        launchApp(youtube)

        assertEquals(emptyList(), appCatalog.launched)
        assertEquals(1, screenLocker.lockCount)
    }

    @Test
    fun launchesAnAlwaysAllowedAppWithoutGrantedTime() {
        val appCatalog = FakeAppCatalog()
        val appAccessStore = FakeAppAccessStore(mapOf(youtube.id to AppAccess.Always))
        val launchApp = LaunchApp(appAccessStore, FakeClock(now), appCatalog, FakeScreenLocker())

        launchApp(youtube)

        assertEquals(listOf(youtube), appCatalog.launched)
    }
}
