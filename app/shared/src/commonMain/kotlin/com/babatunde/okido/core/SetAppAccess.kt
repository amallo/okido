package com.babatunde.okido.core

class SetAppAccess(private val appAccessStore: AppAccessStore) {
    operator fun invoke(app: LaunchableApp, access: AppAccess) {
        appAccessStore.setAccess(app.id, access)
    }
}
