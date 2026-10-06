package com.babatunde.okido.core

import com.babatunde.okido.core.providers.AccessStore
import com.babatunde.okido.core.providers.Clock
import com.babatunde.okido.core.providers.ParentPin
import kotlin.time.Duration

class GrantExtraTime(
    private val parentPin: ParentPin,
    private val accessStore: AccessStore,
    private val clock: Clock,
) {
    operator fun invoke(pin: String, duration: Duration): GrantExtraTimeResult {
        if (!parentPin.verify(pin)) return GrantExtraTimeResult.InvalidPin
        val now = clock.now()
        val start = maxOf(now, accessStore.unlockedUntil() ?: now)
        accessStore.unlockUntil(start + duration)
        return GrantExtraTimeResult.Granted
    }
}

sealed interface GrantExtraTimeResult {
    data object Granted : GrantExtraTimeResult
    data object InvalidPin : GrantExtraTimeResult
}
