package com.babatunde.okido.core

/** The system service that keeps the child out of apps that are not allowed. */
interface BlockingService {
    fun isEnabled(): Boolean
    fun openSettings()
}
