package com.babatunde.okido.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.babatunde.okido.core.AppSetting
import com.babatunde.okido.core.LaunchableApp
import com.babatunde.okido.ui.components.PinPad

@Composable
@Preview
fun ParentSettingsScreen(
    state: ParentSettingsUiState = ParentSettingsUiState.Unlocked(
        listOf(
            AppSetting(LaunchableApp(id = "com.example.camera", label = "Appareil photo"), allowed = false),
            AppSetting(LaunchableApp(id = "com.example.clock", label = "Horloge"), allowed = true),
        ),
    ),
    onPinChange: (String) -> Unit = {},
    onSubmitPin: () -> Unit = {},
    onToggle: (AppSetting, Boolean) -> Unit = { _, _ -> },
    onClose: () -> Unit = {},
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is ParentSettingsUiState.PinRequired -> PinStep(state, onPinChange, onSubmitPin, onClose)
            is ParentSettingsUiState.Unlocked -> AppsStep(state.apps, onToggle, onClose)
        }
    }
}

@Composable
private fun PinStep(
    state: ParentSettingsUiState.PinRequired,
    onPinChange: (String) -> Unit,
    onSubmitPin: () -> Unit,
    onClose: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize().safeContentPadding()) {
        TextButton(onClick = onClose, modifier = Modifier.align(Alignment.TopStart)) {
            Text("Annuler", style = MaterialTheme.typography.bodyLarge)
        }
        PinPad(
            title = "Réglages parent",
            subtitle = "Saisis ton code parent",
            pin = state.pin,
            onPinChange = onPinChange,
            onSubmit = onSubmitPin,
            error = state.invalidPin,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun AppsStep(
    apps: List<AppSetting>,
    onToggle: (AppSetting, Boolean) -> Unit,
    onClose: () -> Unit,
) {
    Column(modifier = Modifier.safeContentPadding().padding(16.dp)) {
        Text("Apps autorisées", style = MaterialTheme.typography.headlineSmall)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(apps, key = { it.app.id }) { setting ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = setting.app.label,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f),
                    )
                    Switch(checked = setting.allowed, onCheckedChange = { onToggle(setting, it) })
                }
                HorizontalDivider()
            }
        }
        Button(onClick = onClose, modifier = Modifier.fillMaxWidth()) {
            Text("Terminé")
        }
    }
}
