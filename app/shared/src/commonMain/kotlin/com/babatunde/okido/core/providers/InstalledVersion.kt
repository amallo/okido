package com.babatunde.okido.core.providers

import com.babatunde.okido.core.AppVersion

/** The version of the app installed on the device. */
interface InstalledVersion {
    fun get(): AppVersion
}
