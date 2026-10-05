package com.babatunde.okido.core

import kotlin.time.Duration

interface ExtraTimeLedger {
    fun add(duration: Duration)
}
