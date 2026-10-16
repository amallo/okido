package com.babatunde.okido.core.usecases

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
        val unlockedUntil = accessStore.unlockedUntil()
        if (unlockedUntil == null || unlockedUntil <= now) accessStore.startSession(now)
        accessStore.unlockUntil(maxOf(now, unlockedUntil ?: now) + duration)
        return GrantExtraTimeResult.Granted
    }
}

sealed interface GrantExtraTimeResult {
    data object Granted : GrantExtraTimeResult
    data object InvalidPin : GrantExtraTimeResult
}
