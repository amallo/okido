package com.babatunde.okido.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.ui.graphics.ImageBitmap
import com.babatunde.okido.core.AllowedApps
import com.babatunde.okido.core.AlwaysAllowedApps
import com.babatunde.okido.core.AppIcons
import com.babatunde.okido.core.LaunchApp
import com.babatunde.okido.core.LaunchableApp
import com.babatunde.okido.core.LockNow
import com.babatunde.okido.core.RemainingTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

sealed interface HomeUiState {
    data class Locked(
        val alwaysApps: List<LaunchableApp> = emptyList(),
        val icons: Map<String, ImageBitmap> = emptyMap(),
    ) : HomeUiState
    data class Unlocked(
        val apps: List<LaunchableApp>,
        val remaining: Duration,
        val icons: Map<String, ImageBitmap> = emptyMap(),
    ) : HomeUiState
}

class HomeViewModel(
    private val remainingTime: RemainingTime,
    private val allowedApps: AllowedApps,
    private val alwaysAllowedApps: AlwaysAllowedApps,
    private val appIcons: AppIcons,
    private val launchApp: LaunchApp,
    private val lockNow: LockNow,
) : ViewModel() {
    private val apps = MutableStateFlow(LoadedApps())
    private val icons = MutableStateFlow<Map<String, ImageBitmap>>(emptyMap())
    private val refreshes = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    // Ticks only while the UI collects the state, so nothing runs when Okido is in the background.
    private val ticks = flow {
        while (true) {
            emit(Unit)
            delay(30.seconds)
        }
    }

    val state: StateFlow<HomeUiState> =
        combine(merge(ticks, refreshes).map { remainingTime() }, apps, icons) { remaining, apps, icons ->
            if (remaining > Duration.ZERO) {
                HomeUiState.Unlocked(apps.allowed, remaining, icons)
            } else {
                HomeUiState.Locked(apps.always, icons)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState.Locked())

    fun refresh() {
        refreshes.tryEmit(Unit)
        viewModelScope.launch {
            val (loadedApps, loadedIcons) = withContext(Dispatchers.IO) {
                val allowed = allowedApps()
                // Always allowed apps are among the allowed ones, so their icons are loaded too.
                LoadedApps(allowed, alwaysAllowedApps()) to appIcons(allowed).decodeIcons()
            }
            icons.value = loadedIcons
            apps.value = loadedApps
        }
    }

    fun onLaunch(app: LaunchableApp) {
        launchApp(app)
        refresh()
    }

    fun onLockNow() {
        lockNow()
        refresh()
    }
}

private data class LoadedApps(
    val allowed: List<LaunchableApp> = emptyList(),
    val always: List<LaunchableApp> = emptyList(),
)
