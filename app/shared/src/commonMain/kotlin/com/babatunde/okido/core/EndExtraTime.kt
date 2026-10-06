package com.babatunde.okido.core

import com.babatunde.okido.core.providers.ScreenLocker

class EndExtraTime(private val screenLocker: ScreenLocker) {
    operator fun invoke() {
        screenLocker.lock()
    }
}
