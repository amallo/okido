package com.babatunde.okido.core

class ChangeParentPin(private val parentPin: ParentPin) {
    operator fun invoke(current: String, new: String, confirmation: String): ChangeParentPinResult {
        if (!parentPin.verify(current)) return ChangeParentPinResult.InvalidPin
        if (new != confirmation) return ChangeParentPinResult.Mismatch
        parentPin.save(new)
        return ChangeParentPinResult.Changed
    }
}
