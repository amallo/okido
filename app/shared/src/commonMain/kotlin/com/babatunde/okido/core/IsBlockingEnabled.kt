package com.babatunde.okido.core

import com.babatunde.okido.core.providers.BlockingService

class IsBlockingEnabled(private val blockingService: BlockingService) {
    operator fun invoke(): Boolean = blockingService.isEnabled()
}
