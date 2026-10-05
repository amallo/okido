package com.babatunde.okido.core

import kotlin.time.Duration

interface ExtraTimeScheduler {
    fun scheduleEnd(after: Duration)
}
