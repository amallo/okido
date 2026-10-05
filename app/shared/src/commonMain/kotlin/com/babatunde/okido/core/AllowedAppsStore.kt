package com.babatunde.okido.core

import kotlin.time.Instant

interface AllowedAppsStore {
    fun allowedAppIds(): Set<String>
    fun allow(id: String)
    fun disallow(id: String)
    fun canUse(appId: String, now: Instant): Boolean
}
