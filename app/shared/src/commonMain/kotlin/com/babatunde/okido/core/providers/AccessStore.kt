package com.babatunde.okido.core.providers

import kotlin.time.Instant

interface AccessStore {
    fun unlockedUntil(): Instant?
    fun unlockUntil(instant: Instant)
    fun sessionStart(): Instant?
    fun startSession(instant: Instant)
}
