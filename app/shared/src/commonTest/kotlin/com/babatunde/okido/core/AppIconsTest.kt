package com.babatunde.okido.core

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class AppIconsTest {

    private val clock = LaunchableApp(id = "com.example.clock", label = "Horloge")

    @Test
    fun givesTheCatalogIconOfEachApp() {
        val png = byteArrayOf(1, 2, 3)
        val appIcons = AppIcons(FakeAppCatalog(icons = mapOf(clock.id to png)))

        val icons = appIcons(listOf(clock))

        assertContentEquals(png, icons[clock.id])
    }

    @Test
    fun skipsAppsWithoutIcon() {
        val appIcons = AppIcons(FakeAppCatalog())

        val icons = appIcons(listOf(clock))

        assertEquals(emptySet(), icons.keys)
    }
}
