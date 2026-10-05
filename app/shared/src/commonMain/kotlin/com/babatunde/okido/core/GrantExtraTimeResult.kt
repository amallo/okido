package com.babatunde.okido.core

sealed interface GrantExtraTimeResult {
    data object Granted : GrantExtraTimeResult
    data object InvalidPin : GrantExtraTimeResult
}
