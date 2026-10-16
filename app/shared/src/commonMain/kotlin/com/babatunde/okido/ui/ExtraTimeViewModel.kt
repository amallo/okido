package com.babatunde.okido.ui

import androidx.lifecycle.ViewModel
import com.babatunde.okido.core.usecases.GrantExtraTime
import com.babatunde.okido.core.usecases.GrantExtraTimeResult
import com.babatunde.okido.core.usecases.VerifyParentPin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

/** Durations a parent can grant; [debug] builds add one minute to try the lock quickly. */
fun grantableDurations(debug: Boolean = false): List<Duration> =
    listOfNotNull(1.minutes.takeIf { debug }, 5.minutes, 15.minutes, 30.minutes, 1.hours)

/** First the parent PIN, then, once [pinAccepted], the duration to grant. */
data class ExtraTimeUiState(
    val pin: String = "",
    val pinAccepted: Boolean = false,
    val durations: List<Duration> = grantableDurations(),
    val result: GrantExtraTimeResult? = null,
)

class ExtraTimeViewModel(
    private val verifyParentPin: VerifyParentPin,
    private val grantExtraTime: GrantExtraTime,
    durations: List<Duration>,
) : ViewModel() {
    private val _state = MutableStateFlow(ExtraTimeUiState(durations = durations))
    val state: StateFlow<ExtraTimeUiState> = _state.asStateFlow()

    fun onPinChange(pin: String) {
        _state.update { it.copy(pin = pin, result = null) }
    }

    fun onSubmitPin() {
        _state.update {
            if (verifyParentPin(it.pin)) it.copy(pinAccepted = true) else it.copy(pin = "", result = GrantExtraTimeResult.InvalidPin)
        }
    }

    /** The PIN is checked again when granting, so the duration step alone never grants time. */
    fun onGrant(duration: Duration) {
        val result = grantExtraTime(pin = _state.value.pin, duration = duration)
        _state.update { it.copy(pin = "", pinAccepted = false, result = result) }
    }

    fun onCancel() {
        _state.update { it.copy(pin = "", pinAccepted = false, result = null) }
    }

    fun clearResult() {
        _state.update { it.copy(result = null) }
    }
}
