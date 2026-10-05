package com.babatunde.okido.infra

import com.babatunde.okido.core.DeviceInfo
import org.koin.dsl.module

actual val infraModule = module {
    single<DeviceInfo> { AndroidDeviceInfo() }
}
