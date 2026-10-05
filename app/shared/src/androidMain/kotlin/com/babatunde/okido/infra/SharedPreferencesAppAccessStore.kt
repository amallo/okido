package com.babatunde.okido.infra

import android.content.SharedPreferences
import com.babatunde.okido.core.AccessStore
import com.babatunde.okido.core.AppAccess
import com.babatunde.okido.core.AppAccessStore
import kotlin.time.Instant

class SharedPreferencesAppAccessStore(
    private val preferences: SharedPreferences,
    private val accessStore: AccessStore,
) : AppAccessStore {
    override fun accesses(): Map<String, AppAccess> =
        appIds(KEY_TIMED_APP_IDS).associateWith { AppAccess.Timed } +
            appIds(KEY_ALWAYS_APP_IDS).associateWith { AppAccess.Always }

    override fun setAccess(appId: String, access: AppAccess) {
        preferences.edit()
            .putStringSet(KEY_TIMED_APP_IDS, appIds(KEY_TIMED_APP_IDS).with(appId, access == AppAccess.Timed))
            .putStringSet(KEY_ALWAYS_APP_IDS, appIds(KEY_ALWAYS_APP_IDS).with(appId, access == AppAccess.Always))
            .apply()
    }

    override fun canUse(appId: String, now: Instant): Boolean =
        (accesses()[appId] ?: AppAccess.Blocked).isOpen(now, accessStore.unlockedUntil())

    // getStringSet's result must not be modified, so always work on a copy.
    private fun appIds(key: String): Set<String> = preferences.getStringSet(key, emptySet()).orEmpty().toSet()

    private fun Set<String>.with(appId: String, present: Boolean): Set<String> =
        if (present) this + appId else this - appId

    private companion object {
        // Timed apps keep the key of the former on/off setting, so apps allowed before stay allowed.
        const val KEY_TIMED_APP_IDS = "allowed_app_ids"
        const val KEY_ALWAYS_APP_IDS = "always_app_ids"
    }
}
