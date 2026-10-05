package com.babatunde.okido.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.babatunde.okido.core.AppSetting
import com.babatunde.okido.core.AppSettings
import com.babatunde.okido.core.SetAppAllowed
import com.babatunde.okido.core.UnlockParentSettings
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
    data class Unlocked(val apps: List<AppSetting>) : ParentSettingsUiState
}

class ParentSettingsViewModel(
    private val unlockParentSettings: UnlockParentSettings,
    private val appSettings: AppSettings,
    private val setAppAllowed: SetAppAllowed,
) : ViewModel() {
    private val _state = MutableStateFlow<ParentSettingsUiState>(ParentSettingsUiState.PinRequired())
    val state: StateFlow<ParentSettingsUiState> = _state.asStateFlow()

    fun onPinChange(pin: String) {
        _state.value = ParentSettingsUiState.PinRequired(pin = pin)
    }

    fun onSubmitPin() {
        val current = _state.value as? ParentSettingsUiState.PinRequired ?: return
        if (!unlockParentSettings(current.pin)) {
            _state.value = ParentSettingsUiState.PinRequired(invalidPin = true)
            return
        }
        viewModelScope.launch {
            val apps = withContext(Dispatchers.IO) { appSettings() }
            _state.value = ParentSettingsUiState.Unlocked(apps)
        }
    }

    fun onToggle(setting: AppSetting, allowed: Boolean) {
        setAppAllowed(setting.app, allowed)
        _state.update { state ->
            if (state !is ParentSettingsUiState.Unlocked) return@update state
            state.copy(apps = state.apps.map { if (it.app == setting.app) it.copy(allowed = allowed) else it })
        }
    }

    fun close() {
        _state.value = ParentSettingsUiState.PinRequired()
    }
}
