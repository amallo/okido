package com.babatunde.okido.core

import kotlin.time.Duration

class GrantExtraTime(
    private val pinVerifier: PinVerifier,
    private val ledger: ExtraTimeLedger,
    private val scheduler: ExtraTimeScheduler,
) {
    operator fun invoke(pin: String, duration: Duration): GrantExtraTimeResult {
        if (!pinVerifier.verify(pin)) return GrantExtraTimeResult.InvalidPin
        ledger.add(duration)
        scheduler.scheduleEnd(duration)
        return GrantExtraTimeResult.Granted
    }
}
