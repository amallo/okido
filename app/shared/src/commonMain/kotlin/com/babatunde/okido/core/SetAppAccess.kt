package com.babatunde.okido.core

import com.babatunde.okido.core.providers.AppAccessStore

class SetAppAccess(private val appAccessStore: AppAccessStore) {
    operator fun invoke(app: LaunchableApp, access: AppAccess) {
        appAccessStore.setAccess(app.id, access)
    }
}
