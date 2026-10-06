package com.babatunde.okido.core

import com.babatunde.okido.core.providers.AccessStore
import com.babatunde.okido.core.providers.Clock

/** The share of the granted time already used, from 0 when the session starts to 1 when it ends. */
class SessionProgress(
    private val accessStore: AccessStore,
    private val clock: Clock,
) {
    operator fun invoke(): Double {
        val end = accessStore.unlockedUntil() ?: return 1.0
        val start = accessStore.sessionStart() ?: return 0.0
        return ((clock.now() - start) / (end - start)).coerceAtMost(1.0)
    }
}
