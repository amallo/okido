package com.babatunde.okido.ui

import androidx.lifecycle.ViewModel
import com.babatunde.okido.core.CreateParentPin
import com.babatunde.okido.core.CreateParentPinResult
import com.babatunde.okido.core.NextOnboardingStep
import com.babatunde.okido.core.OnboardingStep
import com.babatunde.okido.core.OpenBlockingSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class OnboardingPage { Welcome, Guidelines, Privacy, CreatePin, ConfirmPin, EnableBlocking, Done }

private val introductionPages = listOf(OnboardingPage.Welcome, OnboardingPage.Guidelines, OnboardingPage.Privacy, OnboardingPage.CreatePin, OnboardingPage.ConfirmPin)

data class OnboardingUiState(
    val page: OnboardingPage = OnboardingPage.Welcome,
    val pin: String = "",
    val chosenPin: String = "",
    val mismatch: Boolean = false,
    val blockingDeclined: Boolean = false,
)

class OnboardingViewModel(
    private val nextOnboardingStep: NextOnboardingStep,
    private val createParentPin: CreateParentPin,
    private val openBlockingSettings: OpenBlockingSettings,
) : ViewModel() {
    private val _state = MutableStateFlow(OnboardingUiState(page = nextPage(OnboardingPage.Welcome)))
    val state: StateFlow<OnboardingUiState> = _state.asStateFlow()

    /** Called when Okido comes back to the foreground, e.g. after enabling blocking in Android settings. */
    fun refresh() {
        _state.update { it.copy(page = nextPage(it.page)) }
    }

    fun onContinue() {
        _state.update { state ->
            when (state.page) {
                OnboardingPage.Welcome -> state.copy(page = OnboardingPage.Guidelines)
                OnboardingPage.Guidelines -> state.copy(page = OnboardingPage.Privacy)
                OnboardingPage.Privacy -> state.copy(page = OnboardingPage.CreatePin)
                else -> state
            }
        }
    }

    fun onBack() {
        _state.update { state ->
            when (state.page) {
                OnboardingPage.Guidelines -> state.copy(page = OnboardingPage.Welcome)
                OnboardingPage.Privacy -> state.copy(page = OnboardingPage.Guidelines)
                OnboardingPage.CreatePin -> state.copy(page = OnboardingPage.Privacy, pin = "", mismatch = false)
                OnboardingPage.ConfirmPin -> state.copy(page = OnboardingPage.CreatePin, pin = "", chosenPin = "")
                else -> state
            }
        }
    }

    fun onPinChange(pin: String) {
        _state.update { it.copy(pin = pin, mismatch = false) }
    }

    fun onSubmitPin() {
        val state = _state.value
        when (state.page) {
            OnboardingPage.CreatePin ->
                _state.value = state.copy(page = OnboardingPage.ConfirmPin, chosenPin = state.pin, pin = "")
            OnboardingPage.ConfirmPin -> when (createParentPin(state.chosenPin, state.pin)) {
                CreateParentPinResult.Mismatch ->
                    _state.value = state.copy(page = OnboardingPage.CreatePin, pin = "", chosenPin = "", mismatch = true)
                CreateParentPinResult.Created, CreateParentPinResult.AlreadySet ->
                    _state.value = OnboardingUiState(page = nextPage(state.page))
            }
            else -> Unit
        }
    }

    fun onOpenSettings() {
        _state.update { it.copy(blockingDeclined = false) }
        openBlockingSettings()
    }

    fun onDecline() {
        _state.update { it.copy(blockingDeclined = true) }
    }

    /** Keeps the parent on their introduction page until the PIN exists, then follows the core. */
    private fun nextPage(current: OnboardingPage): OnboardingPage = when (nextOnboardingStep()) {
        OnboardingStep.Introduction -> if (current in introductionPages) current else OnboardingPage.Welcome
        OnboardingStep.EnableBlocking -> OnboardingPage.EnableBlocking
        OnboardingStep.Done -> OnboardingPage.Done
    }
}
