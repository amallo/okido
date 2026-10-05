package com.babatunde.okido.infra

import android.os.Build
import com.babatunde.okido.core.DeviceInfo

class AndroidDeviceInfo : DeviceInfo {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}
