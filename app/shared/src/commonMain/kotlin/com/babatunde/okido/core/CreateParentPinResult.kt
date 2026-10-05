package com.babatunde.okido.core

sealed interface CreateParentPinResult {
    data object Created : CreateParentPinResult
    data object Mismatch : CreateParentPinResult
    data object AlreadySet : CreateParentPinResult
}
