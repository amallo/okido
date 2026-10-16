package com.babatunde.okido.core.usecases

import com.babatunde.okido.core.providers.ParentPin

class VerifyParentPin(private val parentPin: ParentPin) {
    operator fun invoke(pin: String): Boolean = parentPin.verify(pin)
}
