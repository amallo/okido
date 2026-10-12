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
    onboardingViewModel: OnboardingViewModel = koinViewModel(),
) {
    val home by homeViewModel.state.collectAsStateWithLifecycle()
    val extraTime by extraTimeViewModel.state.collectAsStateWithLifecycle()
    val parentSettings by parentSettingsViewModel.state.collectAsStateWithLifecycle()
    val onboarding by onboardingViewModel.state.collectAsStateWithLifecycle()
    var askingMoreTime by remember { mutableStateOf(false) }
    var showingParentSettings by remember { mutableStateOf(false) }

    fun closeParentSettings() {
        showingParentSettings = false
        parentSettingsViewModel.close()
        homeViewModel.refresh()
    }

    LifecycleResumeEffect(Unit) {
        onboardingViewModel.refresh()
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
        if (onboarding.page != OnboardingPage.Done) {
            OnboardingScreen(
                state = onboarding,
                onContinue = onboardingViewModel::onContinue,
                onBack = onboardingViewModel::onBack,
                onPinChange = onboardingViewModel::onPinChange,
                onSubmitPin = onboardingViewModel::onSubmitPin,
                onAccessChange = onboardingViewModel::onAccessChange,
                onOpenSettings = onboardingViewModel::onOpenSettings,
                onDecline = onboardingViewModel::onDecline,
            )
            return@OkidoTheme
        }
        if (showingParentSettings) {
            ParentSettingsScreen(
                state = parentSettings,
                onPinChange = parentSettingsViewModel::onPinChange,
                onSubmitPin = parentSettingsViewModel::onSubmitPin,
                onAccessChange = parentSettingsViewModel::onAccessChange,
                onChangePin = parentSettingsViewModel::onChangePin,
                onChangePinInput = parentSettingsViewModel::onChangePinInput,
                onSubmitChangePin = parentSettingsViewModel::onSubmitChangePin,
                onCancelChangePin = parentSettingsViewModel::onCancelChangePin,
                onClose = ::closeParentSettings,
            )
            return@OkidoTheme
        }
        when (val state = home) {
            is HomeUiState.Locked -> ExtraTimeScreen(
                title = "Temps écoulé",
                state = extraTime,
                alwaysApps = state.alwaysApps,
                icons = state.icons,
                onLaunch = homeViewModel::onLaunch,
                onPinChange = extraTimeViewModel::onPinChange,
                onSubmitPin = extraTimeViewModel::onSubmitPin,
                onGrant = extraTimeViewModel::onGrant,
                onCancel = extraTimeViewModel::onCancel,
                onParentSettings = { showingParentSettings = true },
            )
            is HomeUiState.Unlocked -> if (askingMoreTime) {
                ExtraTimeScreen(
                    title = "Plus de temps",
                    state = extraTime,
                    onPinChange = extraTimeViewModel::onPinChange,
                    onSubmitPin = extraTimeViewModel::onSubmitPin,
                    onGrant = extraTimeViewModel::onGrant,
                    onCancel = extraTimeViewModel::onCancel,
                    onBack = { askingMoreTime = false },
                )
            } else {
                HomeScreen(
                    apps = state.apps,
                    remaining = state.remaining,
                    sleepiness = state.sleepiness,
                    icons = state.icons,
                    onLaunch = homeViewModel::onLaunch,
                    onMoreTime = { askingMoreTime = true },
                    onLockNow = homeViewModel::onLockNow,
                    onParentSettings = { showingParentSettings = true },
                )
            }
        }
    }
}
