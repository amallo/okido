package com.babatunde.okido.core.providers

import kotlin.time.Duration

interface ExtraTimeScheduler {
    fun scheduleEnd(after: Duration)
}
