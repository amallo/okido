package com.babatunde.okido.core.usecases

import com.babatunde.okido.core.providers.ParentPin

class ChangeParentPin(private val parentPin: ParentPin) {
    operator fun invoke(current: String, new: String, confirmation: String): ChangeParentPinResult {
        if (!parentPin.verify(current)) return ChangeParentPinResult.InvalidPin
        if (new != confirmation) return ChangeParentPinResult.Mismatch
        parentPin.save(new)
        return ChangeParentPinResult.Changed
    }
}

sealed interface ChangeParentPinResult {
    data object Changed : ChangeParentPinResult
    data object InvalidPin : ChangeParentPinResult
    data object Mismatch : ChangeParentPinResult
}
