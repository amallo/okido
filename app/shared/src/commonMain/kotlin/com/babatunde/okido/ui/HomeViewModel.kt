package com.babatunde.okido.ui

import androidx.lifecycle.ViewModel
import com.babatunde.okido.core.AppCatalog
import com.babatunde.okido.core.CanUseApps
import com.babatunde.okido.core.LaunchApp
import com.babatunde.okido.core.LaunchableApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface HomeUiState {
    data object Locked : HomeUiState
    data class Unlocked(val apps: List<LaunchableApp>) : HomeUiState
}

class HomeViewModel(
    private val canUseApps: CanUseApps,
    private val appCatalog: AppCatalog,
    private val launchApp: LaunchApp,
) : ViewModel() {
    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Locked)
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    fun refresh() {
        _state.value = if (canUseApps()) {
            HomeUiState.Unlocked(appCatalog.launchableApps())
        } else {
            HomeUiState.Locked
        }
    }

    fun onLaunch(app: LaunchableApp) {
        launchApp(app)
        refresh()
    }
}
