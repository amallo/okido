package com.babatunde.okido.core

import com.babatunde.okido.core.providers.BlockingService

class OpenBlockingSettings(private val blockingService: BlockingService) {
    operator fun invoke() = blockingService.openSettings()
}
