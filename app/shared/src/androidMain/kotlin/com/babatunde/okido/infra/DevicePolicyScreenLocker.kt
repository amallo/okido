package com.babatunde.okido.infra

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import com.babatunde.okido.core.ScreenLocker

class DevicePolicyScreenLocker(private val context: Context) : ScreenLocker {
    override fun lock() {
        val devicePolicyManager = context.getSystemService(DevicePolicyManager::class.java)
        val admin = ComponentName(context, OkidoDeviceAdminReceiver::class.java)
        if (devicePolicyManager.isAdminActive(admin)) {
            devicePolicyManager.lockNow()
        }
    }
}
