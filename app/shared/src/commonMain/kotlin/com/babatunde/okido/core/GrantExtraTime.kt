package com.babatunde.okido.core

import kotlin.time.Duration

class GrantExtraTime(
    private val pinVerifier: PinVerifier,
    private val accessStore: AccessStore,
    private val scheduler: ExtraTimeScheduler,
    private val clock: Clock,
) {
    operator fun invoke(pin: String, duration: Duration): GrantExtraTimeResult {
        if (!pinVerifier.verify(pin)) return GrantExtraTimeResult.InvalidPin
        val now = clock.now()
        val start = maxOf(now, accessStore.unlockedUntil() ?: now)
        val end = start + duration
        accessStore.unlockUntil(end)
        scheduler.scheduleEnd(end - now)
        return GrantExtraTimeResult.Granted
    }
}
