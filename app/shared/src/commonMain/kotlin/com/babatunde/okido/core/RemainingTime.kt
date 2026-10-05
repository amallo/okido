package com.babatunde.okido.core

import kotlin.time.Duration

class RemainingTime(
    private val accessStore: AccessStore,
    private val clock: Clock,
) {
    operator fun invoke(): Duration {
        val unlockedUntil = accessStore.unlockedUntil() ?: return Duration.ZERO
        return (unlockedUntil - clock.now()).coerceAtLeast(Duration.ZERO)
    }
}
