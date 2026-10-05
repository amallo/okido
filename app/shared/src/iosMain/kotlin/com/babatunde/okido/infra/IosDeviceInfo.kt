package com.babatunde.okido.infra

import com.babatunde.okido.core.DeviceInfo
import platform.UIKit.UIDevice

class IosDeviceInfo : DeviceInfo {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}
