package com.babatunde.okido.core

class UnlockParentSettings(private val parentPin: ParentPin) {
    operator fun invoke(pin: String): Boolean = parentPin.verify(pin)
}
