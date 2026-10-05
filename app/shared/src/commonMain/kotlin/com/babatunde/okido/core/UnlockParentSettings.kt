package com.babatunde.okido.core

class UnlockParentSettings(private val pinVerifier: PinVerifier) {
    operator fun invoke(pin: String): Boolean = pinVerifier.verify(pin)
}
