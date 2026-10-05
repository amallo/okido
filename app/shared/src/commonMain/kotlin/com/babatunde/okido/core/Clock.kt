package com.babatunde.okido.core

import kotlin.time.Instant

interface Clock {
    fun now(): Instant
}
