package com.babatunde.okido.core

sealed interface ChangeParentPinResult {
    data object Changed : ChangeParentPinResult
    data object InvalidPin : ChangeParentPinResult
    data object Mismatch : ChangeParentPinResult
}
