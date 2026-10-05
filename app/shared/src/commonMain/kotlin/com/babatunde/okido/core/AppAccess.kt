package com.babatunde.okido.core

import kotlin.time.Instant

enum class AppAccess {
    Blocked,
    Timed,
    Always;

    fun isOpen(now: Instant, unlockedUntil: Instant?): Boolean = when (this) {
        Always -> true
        Timed -> unlockedUntil != null && now < unlockedUntil
        Blocked -> false
    }
}
