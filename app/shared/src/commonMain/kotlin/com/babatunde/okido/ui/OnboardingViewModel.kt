package com.babatunde.okido.ui

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.babatunde.okido.core.AppAccess
import com.babatunde.okido.core.AppSetting
import com.babatunde.okido.core.AppSettings
import com.babatunde.okido.core.CreateParentPin
import com.babatunde.okido.core.CreateParentPinResult
import com.babatunde.okido.core.FinishChoosingApps
import com.babatunde.okido.core.NextOnboardingStep
import com.babatunde.okido.core.OnboardingStep
import com.babatunde.okido.core.OpenBlockingSettings
import com.babatunde.okido.core.OpenHomeSettings
import com.babatunde.okido.core.ReadBlockingWarning
import com.babatunde.okido.core.SetAppAccess
import com.babatunde.okido.core.providers.AppIconLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class OnboardingPage { Welcome, Guidelines, Privacy, CreatePin, ConfirmPin, ChooseApps, EnableBlocking, BlockingCanStop, SetAsHome, Done }

private val introductionPages = listOf(OnboardingPage.Welcome, OnboardingPage.Guidelines, OnboardingPage.Privacy, OnboardingPage.CreatePin, OnboardingPage.ConfirmPin)

data class OnboardingUiState(
    val page: OnboardingPage = OnboardingPage.Welcome,
    val pin: String = "",
    val chosenPin: String = "",
    val mismatch: Boolean = false,
    val blockingDeclined: Boolean = false,
    val apps: List<AppSetting> = emptyList(),
    val icons: Map<String, ImageBitmap> = emptyMap(),
)

class OnboardingViewModel(
    private val nextOnboardingStep: NextOnboardingStep,
    private val createParentPin: CreateParentPin,
    private val openBlockingSettings: OpenBlockingSettings,
    private val openHomeSettings: OpenHomeSettings,
    private val appSettings: AppSettings,
    private val appIconLoader: AppIconLoader,
    private val setAppAccess: SetAppAccess,
    private val finishChoosingApps: FinishChoosingApps,
    private val readBlockingWarning: ReadBlockingWarning,
) : ViewModel() {
    private val _state = MutableStateFlow(OnboardingUiState(page = nextPage(OnboardingPage.Welcome)))
    val state: StateFlow<OnboardingUiState> = _state.asStateFlow()

    init {
        loadAppsIfChoosing()
    }

    /** Called when Okido comes back to the foreground, e.g. after enabling blocking or choosing the home app in Android settings. */
    fun refresh() {
        _state.update { it.copy(page = nextPage(it.page)) }
    }

    fun onContinue() {
        _state.update { state ->
            when (state.page) {
                OnboardingPage.Welcome -> state.copy(page = OnboardingPage.Guidelines)
                OnboardingPage.Guidelines -> state.copy(page = OnboardingPage.Privacy)
                OnboardingPage.Privacy -> state.copy(page = OnboardingPage.CreatePin)
                OnboardingPage.ChooseApps -> {
                    finishChoosingApps()
                    state.copy(page = nextPage(state.page))
                }
                OnboardingPage.BlockingCanStop -> {
                    readBlockingWarning()
                    state.copy(page = nextPage(state.page))
                }
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
                CreateParentPinResult.Created, CreateParentPinResult.AlreadySet -> {
                    _state.value = OnboardingUiState(page = nextPage(state.page))
                    loadAppsIfChoosing()
                }
            }
            else -> Unit
        }
    }

    fun onAccessChange(setting: AppSetting, access: AppAccess) {
        setAppAccess(setting.app, access)
        _state.update { state ->
            state.copy(apps = state.apps.map { if (it.app == setting.app) it.copy(access = access) else it })
        }
    }

    fun onOpenSettings() {
        _state.update { it.copy(blockingDeclined = false) }
        openBlockingSettings()
    }

    fun onSetAsHome() {
        openHomeSettings()
    }

    fun onDecline() {
        _state.update { it.copy(blockingDeclined = true) }
    }

    /** Keeps the parent on their introduction page until the PIN exists, then follows the core. */
    private fun nextPage(current: OnboardingPage): OnboardingPage = when (nextOnboardingStep()) {
        OnboardingStep.Introduction -> if (current in introductionPages) current else OnboardingPage.Welcome
        OnboardingStep.ChooseApps -> OnboardingPage.ChooseApps
        OnboardingStep.EnableBlocking -> OnboardingPage.EnableBlocking
        OnboardingStep.BlockingCanStop -> OnboardingPage.BlockingCanStop
        OnboardingStep.SetAsHome -> OnboardingPage.SetAsHome
        OnboardingStep.Done -> OnboardingPage.Done
    }

    private fun loadAppsIfChoosing() {
        if (_state.value.page != OnboardingPage.ChooseApps) return
        // The names come quickly, the icons take a few seconds: show the list first.
        viewModelScope.launch {
            val apps = withContext(Dispatchers.IO) { appSettings() }
            _state.update { it.copy(apps = apps) }
            val icons = withContext(Dispatchers.IO) { appIconLoader.decodeIcons(apps.map { it.app }) }
            _state.update { it.copy(icons = icons) }
        }
    }
}
