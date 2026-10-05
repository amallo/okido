package com.babatunde.okido.core

class OpenBlockingSettings(private val blockingService: BlockingService) {
    operator fun invoke() = blockingService.openSettings()
}
