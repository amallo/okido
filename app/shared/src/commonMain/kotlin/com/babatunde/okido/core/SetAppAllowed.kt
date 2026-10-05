package com.babatunde.okido.core

class SetAppAllowed(private val allowedAppsStore: AllowedAppsStore) {
    operator fun invoke(app: LaunchableApp, allowed: Boolean) {
        if (allowed) allowedAppsStore.allow(app.id) else allowedAppsStore.disallow(app.id)
    }
}
