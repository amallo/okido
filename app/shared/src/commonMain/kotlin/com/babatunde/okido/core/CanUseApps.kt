package com.babatunde.okido.core

class CanUseApps(
    private val accessStore: AccessStore,
    private val clock: Clock,
) {
    operator fun invoke(): Boolean {
        val unlockedUntil = accessStore.unlockedUntil() ?: return false
        return clock.now() < unlockedUntil
    }
}
