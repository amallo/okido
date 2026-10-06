package com.babatunde.okido.infra.providers

import com.babatunde.okido.core.providers.Clock
import kotlin.time.Instant

class DeviceClock : Clock {
    override fun now(): Instant = kotlin.time.Clock.System.now()
}
