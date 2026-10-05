package com.babatunde.okido.core

class EndExtraTime(private val screenLocker: ScreenLocker) {
    operator fun invoke() {
        screenLocker.lock()
    }
}
