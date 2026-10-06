package com.babatunde.okido.core.providers

import kotlin.time.Instant

interface Clock {
    fun now(): Instant
}
