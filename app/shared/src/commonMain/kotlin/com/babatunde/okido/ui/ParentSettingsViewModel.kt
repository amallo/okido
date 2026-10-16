package com.babatunde.okido.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.ui.graphics.ImageBitmap
import com.babatunde.okido.core.AppAccess
import com.babatunde.okido.core.AppSetting
import com.babatunde.okido.core.usecases.AppSettings
import com.babatunde.okido.core.usecases.ChangeParentPin
import com.babatunde.okido.core.usecases.ChangeParentPinResult
import com.babatunde.okido.core.usecases.IsBlockingEnabled
import com.babatunde.okido.core.usecases.OpenBlockingSettings
import com.babatunde.okido.core.usecases.SetAppAccess
import com.babatunde.okido.core.usecases.VerifyParentPin
import com.babatunde.okido.core.providers.AppIconLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface ParentSettingsUiState {
    data class PinRequired(val pin: String = "", val invalidPin: Boolean = false) : ParentSettingsUiState
    data class Unlocked(
        val apps: List<AppSetting>,
        val icons: Map<String, ImageBitmap> = emptyMap(),
        val pinChanged: Boolean = false,
        val blockingEnabled: Boolean = true,
    ) : ParentSettingsUiState

    /** Asks the current code, then the new one twice; [settings] is where the parent goes back to. */
    data class ChangingPin(
        val settings: Unlocked,
        val step: PinChangeStep = PinChangeStep.Current,
        val pin: String = "",
        val current: String = "",
        val new: String = "",
        val error: PinChangeError? = null,
    ) : ParentSettingsUiState
}

enum class PinChangeStep { Current, New, Confirm }

enum class PinChangeError { InvalidPin, Mismatch }

class ParentSettingsViewModel(
    private val verifyParentPin: VerifyParentPin,
    private val appSettings: AppSettings,
    private val appIconLoader: AppIconLoader,
    private val setAppAccess: SetAppAccess,
    private val changeParentPin: ChangeParentPin,
    private val isBlockingEnabled: IsBlockingEnabled,
    private val openBlockingSettings: OpenBlockingSettings,
) : ViewModel() {
    private val _state = MutableStateFlow<ParentSettingsUiState>(ParentSettingsUiState.PinRequired())
    val state: StateFlow<ParentSettingsUiState> = _state.asStateFlow()

    fun onPinChange(pin: String) {
        _state.value = ParentSettingsUiState.PinRequired(pin = pin)
    }

    fun onSubmitPin() {
        val current = _state.value as? ParentSettingsUiState.PinRequired ?: return
        if (!verifyParentPin(current.pin)) {
            _state.value = ParentSettingsUiState.PinRequired(invalidPin = true)
            return
        }
        viewModelScope.launch {
            _state.value = withContext(Dispatchers.IO) {
                val apps = appSettings()
                ParentSettingsUiState.Unlocked(
                    apps,
                    appIconLoader.decodeIcons(apps.map { it.app }),
                    blockingEnabled = isBlockingEnabled(),
                )
            }
        }
    }

    fun onAccessChange(setting: AppSetting, access: AppAccess) {
        setAppAccess(setting.app, access)
        _state.update { state ->
            if (state !is ParentSettingsUiState.Unlocked) return@update state
            state.copy(apps = state.apps.map { if (it.app == setting.app) it.copy(access = access) else it })
        }
    }

    fun onChangePin() {
        val settings = _state.value as? ParentSettingsUiState.Unlocked ?: return
        _state.value = ParentSettingsUiState.ChangingPin(settings.copy(pinChanged = false))
    }

    fun onChangePinInput(pin: String) {
        _state.update { state ->
            if (state !is ParentSettingsUiState.ChangingPin) return@update state
            state.copy(pin = pin, error = null)
        }
    }

    fun onSubmitChangePin() {
        val state = _state.value as? ParentSettingsUiState.ChangingPin ?: return
        _state.value = when (state.step) {
            // Checked right away, so the parent does not type two new codes for nothing.
            PinChangeStep.Current -> if (verifyParentPin(state.pin)) {
                state.copy(step = PinChangeStep.New, current = state.pin, pin = "")
            } else {
                state.copy(pin = "", error = PinChangeError.InvalidPin)
            }
            PinChangeStep.New -> state.copy(step = PinChangeStep.Confirm, new = state.pin, pin = "")
            PinChangeStep.Confirm -> when (changeParentPin(state.current, state.new, state.pin)) {
                ChangeParentPinResult.Changed -> state.settings.copy(pinChanged = true)
                ChangeParentPinResult.InvalidPin ->
                    ParentSettingsUiState.ChangingPin(state.settings, error = PinChangeError.InvalidPin)
                ChangeParentPinResult.Mismatch ->
                    state.copy(step = PinChangeStep.New, pin = "", new = "", error = PinChangeError.Mismatch)
            }
        }
    }

    fun onCancelChangePin() {
        val state = _state.value as? ParentSettingsUiState.ChangingPin ?: return
        _state.value = state.settings
    }

    /** Opens Android's accessibility settings; [refreshBlocking] reads the new state once the parent is back. */
    fun onEnableBlocking() = openBlockingSettings()

    fun refreshBlocking() {
        _state.update { state ->
            if (state !is ParentSettingsUiState.Unlocked) return@update state
            state.copy(blockingEnabled = isBlockingEnabled())
        }
    }

    fun close() {
        _state.value = ParentSettingsUiState.PinRequired()
    }
}
