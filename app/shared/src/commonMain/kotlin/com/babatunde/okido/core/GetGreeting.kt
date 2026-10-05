package com.babatunde.okido.core

class GetGreeting(private val deviceInfo: DeviceInfo) {
    operator fun invoke(): String = "Hello, ${deviceInfo.name}!"
}
