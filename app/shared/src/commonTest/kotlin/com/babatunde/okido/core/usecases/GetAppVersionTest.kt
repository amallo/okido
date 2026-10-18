package com.babatunde.okido.core.usecases

import com.babatunde.okido.core.AppVersion
import com.babatunde.okido.core.FakeInstalledVersion
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAppVersionTest {

    @Test
    fun givesTheInstalledVersion() {
        val getAppVersion = GetAppVersion(FakeInstalledVersion(AppVersion("0.1.2", 102)))

        assertEquals(AppVersion("0.1.2", 102), getAppVersion())
    }
}
