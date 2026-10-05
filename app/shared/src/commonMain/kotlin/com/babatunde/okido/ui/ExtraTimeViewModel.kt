package com.babatunde.okido.ui

import androidx.lifecycle.ViewModel
import com.babatunde.okido.core.GrantExtraTime
import com.babatunde.okido.core.GrantExtraTimeResult
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

data class ExtraTimeUiState(
    val pin: String = "",
    val durations: List<Duration> = grantableDurations(),
    val duration: Duration = 15.minutes,
    val result: GrantExtraTimeResult? = null,
)

class ExtraTimeViewModel(
    private val grantExtraTime: GrantExtraTime,
    durations: List<Duration>,
) : ViewModel() {
    private val _state = MutableStateFlow(ExtraTimeUiState(durations = durations))
    val state: StateFlow<ExtraTimeUiState> = _state.asStateFlow()

    fun onPinChange(pin: String) {
        _state.update { it.copy(pin = pin, result = null) }
    }

    fun onDurationChange(duration: Duration) {
        _state.update { it.copy(duration = duration, result = null) }
    }

    fun clearResult() {
        _state.update { it.copy(result = null) }
    }

    fun onGrant() {
        val current = _state.value
        val result = grantExtraTime(pin = current.pin, duration = current.duration)
        _state.update { it.copy(pin = "", result = result) }
    }
}
