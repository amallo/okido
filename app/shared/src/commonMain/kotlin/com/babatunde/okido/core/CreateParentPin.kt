package com.babatunde.okido.core

import com.babatunde.okido.core.providers.ParentPin

class CreateParentPin(private val parentPin: ParentPin) {
    operator fun invoke(pin: String, confirmation: String): CreateParentPinResult {
        if (parentPin.isSet()) return CreateParentPinResult.AlreadySet
        if (pin != confirmation) return CreateParentPinResult.Mismatch
        parentPin.save(pin)
        return CreateParentPinResult.Created
    }
}
