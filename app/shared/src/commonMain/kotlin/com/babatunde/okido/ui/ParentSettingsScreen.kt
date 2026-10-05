package com.babatunde.okido.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.babatunde.okido.core.AppSetting
import com.babatunde.okido.core.LaunchableApp
import com.babatunde.okido.ui.components.GroupedRow
import com.babatunde.okido.ui.components.OkidoSwitch
import com.babatunde.okido.ui.components.PinPad
import com.babatunde.okido.ui.theme.OkidoTheme

@Composable
fun ParentSettingsScreen(
    state: ParentSettingsUiState,
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
    Column(modifier = Modifier.fillMaxSize().safeContentPadding()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Réglages parent",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Center),
            )
            TextButton(onClick = onClose, modifier = Modifier.align(Alignment.CenterEnd)) {
                Text("OK", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            }
        }
        LazyColumn(contentPadding = PaddingValues(vertical = 16.dp)) {
            item {
                SectionText(
                    text = "APPS AUTORISÉES · ${apps.count { it.allowed }} SUR ${apps.size}",
                    modifier = Modifier.padding(start = 32.dp, bottom = 6.dp),
                )
            }
            // One lazy item per row, so long app lists stay smooth; the card is rebuilt from rounded ends.
            itemsIndexed(apps, key = { _, setting -> setting.app.id }) { index, setting ->
                val isLast = index == apps.lastIndex
                GroupedRow(
                    label = setting.app.label,
                    leading = { AppIcon(setting.app, modifier = Modifier.size(32.dp)) },
                    trailing = { OkidoSwitch(checked = setting.allowed, onCheckedChange = { onToggle(setting, it) }) },
                    showDivider = !isLast,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(cardSlice(isFirst = index == 0, isLast = isLast))
                        .background(MaterialTheme.colorScheme.surfaceContainer),
                )
            }
            item {
                SectionText(
                    text = "Les apps non autorisées n'apparaissent pas sur l'accueil " +
                        "et sont bloquées, même pendant le temps accordé.",
                    modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun SectionText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
    )
}

private fun cardSlice(isFirst: Boolean, isLast: Boolean): RoundedCornerShape {
    val top = if (isFirst) 12.dp else 0.dp
    val bottom = if (isLast) 12.dp else 0.dp
    return RoundedCornerShape(topStart = top, topEnd = top, bottomStart = bottom, bottomEnd = bottom)
}

@Composable
@Preview
private fun AppsPreview() {
    OkidoTheme {
        ParentSettingsScreen(
            state = ParentSettingsUiState.Unlocked(
                listOf(
                    AppSetting(LaunchableApp(id = "com.example.camera", label = "Appareil photo"), allowed = false),
                    AppSetting(LaunchableApp(id = "com.example.clock", label = "Horloge"), allowed = true),
                ),
            ),
        )
    }
}

@Composable
@Preview
private fun PinPreview() {
    OkidoTheme { ParentSettingsScreen(state = ParentSettingsUiState.PinRequired(pin = "12")) }
}
