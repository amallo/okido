package com.babatunde.okido.core.providers

/** The app the device opens on the home button, which must be Okido so the child never lands on the system launcher. */
interface HomeApp {
    fun isOkido(): Boolean
    fun openSettings()
}
