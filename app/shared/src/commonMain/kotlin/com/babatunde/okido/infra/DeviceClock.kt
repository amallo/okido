package com.babatunde.okido.infra

import com.babatunde.okido.core.Clock
import kotlin.time.Instant

class DeviceClock : Clock {
    override fun now(): Instant = kotlin.time.Clock.System.now()
}
