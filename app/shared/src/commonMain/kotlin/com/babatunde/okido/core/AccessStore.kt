package com.babatunde.okido.core

import kotlin.time.Instant

interface AccessStore {
    fun unlockedUntil(): Instant?
    fun unlockUntil(instant: Instant)
}
