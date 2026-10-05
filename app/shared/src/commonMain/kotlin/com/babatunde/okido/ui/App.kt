package com.babatunde.okido.ui

import com.babatunde.okido.ui.theme.OkidoTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.babatunde.okido.core.GrantExtraTimeResult
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    homeViewModel: HomeViewModel = koinViewModel(),
    extraTimeViewModel: ExtraTimeViewModel = koinViewModel(),
    parentSettingsViewModel: ParentSettingsViewModel = koinViewModel(),
) {
    val home by homeViewModel.state.collectAsStateWithLifecycle()
    val extraTime by extraTimeViewModel.state.collectAsStateWithLifecycle()
    val parentSettings by parentSettingsViewModel.state.collectAsStateWithLifecycle()
    var askingMoreTime by remember { mutableStateOf(false) }
    var showingParentSettings by remember { mutableStateOf(false) }

    fun closeParentSettings() {
        showingParentSettings = false
        parentSettingsViewModel.close()
        homeViewModel.refresh()
    }

    LifecycleResumeEffect(Unit) {
        homeViewModel.refresh()
        onPauseOrDispose {}
    }

    LaunchedEffect(extraTime.result) {
        if (extraTime.result == GrantExtraTimeResult.Granted) {
            askingMoreTime = false
            extraTimeViewModel.clearResult()
            homeViewModel.refresh()
        }
    }

    OkidoTheme {
        if (showingParentSettings) {
            ParentSettingsScreen(
                state = parentSettings,
                onPinChange = parentSettingsViewModel::onPinChange,
                onSubmitPin = parentSettingsViewModel::onSubmitPin,
                onToggle = parentSettingsViewModel::onToggle,
                onClose = ::closeParentSettings,
            )
            return@OkidoTheme
        }
        when (val state = home) {
            HomeUiState.Locked -> ExtraTimeScreen(
                title = "Temps écoulé",
                state = extraTime,
                onPinChange = extraTimeViewModel::onPinChange,
                onDurationChange = extraTimeViewModel::onDurationChange,
                onGrant = extraTimeViewModel::onGrant,
                onParentSettings = { showingParentSettings = true },
            )
            is HomeUiState.Unlocked -> if (askingMoreTime) {
                ExtraTimeScreen(
                    title = "Plus de temps",
                    state = extraTime,
                    onPinChange = extraTimeViewModel::onPinChange,
                    onDurationChange = extraTimeViewModel::onDurationChange,
                    onGrant = extraTimeViewModel::onGrant,
                    onBack = { askingMoreTime = false },
                )
            } else {
                HomeScreen(
                    apps = state.apps,
                    remaining = state.remaining,
                    onLaunch = homeViewModel::onLaunch,
                    onMoreTime = { askingMoreTime = true },
                    onLockNow = homeViewModel::onLockNow,
                    onParentSettings = { showingParentSettings = true },
                )
            }
        }
    }
}
