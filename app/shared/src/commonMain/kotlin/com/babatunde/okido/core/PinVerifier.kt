package com.babatunde.okido.core

interface PinVerifier {
    fun verify(pin: String): Boolean
}
