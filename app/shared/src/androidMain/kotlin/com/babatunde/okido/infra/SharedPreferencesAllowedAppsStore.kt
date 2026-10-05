package com.babatunde.okido.infra

import android.content.SharedPreferences
import com.babatunde.okido.core.AccessStore
import com.babatunde.okido.core.AllowedAppsStore
import kotlin.time.Instant

class SharedPreferencesAllowedAppsStore(
    private val preferences: SharedPreferences,
    private val accessStore: AccessStore,
) : AllowedAppsStore {
    // getStringSet's result must not be modified, so always work on a copy.
    override fun allowedAppIds(): Set<String> =
        preferences.getStringSet(KEY_ALLOWED_APP_IDS, emptySet()).orEmpty().toSet()

    override fun allow(id: String) {
        preferences.edit().putStringSet(KEY_ALLOWED_APP_IDS, allowedAppIds() + id).apply()
    }

    override fun disallow(id: String) {
        preferences.edit().putStringSet(KEY_ALLOWED_APP_IDS, allowedAppIds() - id).apply()
    }

    override fun canUse(appId: String, now: Instant): Boolean {
        val unlockedUntil = accessStore.unlockedUntil() ?: return false
        return appId in allowedAppIds() && now < unlockedUntil
    }

    private companion object {
        const val KEY_ALLOWED_APP_IDS = "allowed_app_ids"
    }
}
