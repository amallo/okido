package com.babatunde.okido.core.usecases

import com.babatunde.okido.core.AppVersion
import com.babatunde.okido.core.providers.InstalledVersion

class GetAppVersion(private val installedVersion: InstalledVersion) {
    operator fun invoke(): AppVersion = installedVersion.get()
}
