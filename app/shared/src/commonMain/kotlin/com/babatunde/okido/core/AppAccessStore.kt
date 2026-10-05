package com.babatunde.okido.core

import kotlin.time.Instant

interface AppAccessStore {
    /** Each app's access; an app missing from the map is blocked. */
    fun accesses(): Map<String, AppAccess>
    fun setAccess(appId: String, access: AppAccess)
    fun canUse(appId: String, now: Instant): Boolean
}
