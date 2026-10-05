package com.babatunde.okido.core

class CreateParentPin(private val parentPin: ParentPin) {
    operator fun invoke(pin: String, confirmation: String): CreateParentPinResult {
        if (parentPin.isSet()) return CreateParentPinResult.AlreadySet
        if (pin != confirmation) return CreateParentPinResult.Mismatch
        parentPin.save(pin)
        return CreateParentPinResult.Created
    }
}
