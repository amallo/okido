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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.babatunde.okido.core.AppAccess
import com.babatunde.okido.core.AppSetting
import com.babatunde.okido.core.LaunchableApp
import com.babatunde.okido.ui.components.GroupedRow
import com.babatunde.okido.ui.components.PinPad
import com.babatunde.okido.ui.components.SegmentedControl
import com.babatunde.okido.ui.theme.OkidoTheme

@Composable
fun ParentSettingsScreen(
    state: ParentSettingsUiState,
    onPinChange: (String) -> Unit = {},
    onSubmitPin: () -> Unit = {},
    onAccessChange: (AppSetting, AppAccess) -> Unit = { _, _ -> },
    onClose: () -> Unit = {},
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is ParentSettingsUiState.PinRequired -> PinStep(state, onPinChange, onSubmitPin, onClose)
            is ParentSettingsUiState.Unlocked -> AppsStep(state.apps, state.icons, onAccessChange, onClose)
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
    icons: Map<String, ImageBitmap>,
    onAccessChange: (AppSetting, AppAccess) -> Unit,
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
                    text = "APPS · ${apps.count { it.access == AppAccess.Timed }} AVEC TEMPS · " +
                        "${apps.count { it.access == AppAccess.Always }} TOUJOURS",
                    modifier = Modifier.padding(start = 32.dp, bottom = 6.dp),
                )
            }
            // One lazy item per row, so long app lists stay smooth; the card is rebuilt from rounded ends.
            itemsIndexed(apps, key = { _, setting -> setting.app.id }) { index, setting ->
                val isLast = index == apps.lastIndex
                AppAccessRow(
                    setting = setting,
                    icon = icons[setting.app.id],
                    onAccessChange = { onAccessChange(setting, it) },
                    showDivider = !isLast,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(cardSlice(isFirst = index == 0, isLast = isLast))
                        .background(MaterialTheme.colorScheme.surfaceContainer),
                )
            }
            item {
                SectionText(
                    text = "Avec temps : utilisable pendant le temps accordé. " +
                        "Toujours : utilisable même quand le temps est écoulé, comme la radio. " +
                        "Bloquée : n'apparaît pas sur l'accueil.",
                    modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 6.dp),
                )
            }
        }
    }
}

private val accessOptions = listOf(AppAccess.Blocked, AppAccess.Timed, AppAccess.Always)

/** An app row with its access below, aligned with the label like an iOS subtitle. */
@Composable
private fun AppAccessRow(
    setting: AppSetting,
    icon: ImageBitmap?,
    onAccessChange: (AppAccess) -> Unit,
    showDivider: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        GroupedRow(
            label = setting.app.label,
            leading = { AppIcon(setting.app, icon, modifier = Modifier.size(32.dp)) },
            showDivider = false,
        )
        SegmentedControl(
            options = accessOptions,
            selected = setting.access,
            onSelect = onAccessChange,
            label = { it.label() },
            modifier = Modifier.padding(start = 60.dp, end = 16.dp, bottom = 12.dp),
        )
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(start = 60.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
            )
        }
    }
}

private fun AppAccess.label(): String = when (this) {
    AppAccess.Blocked -> "Bloquée"
    AppAccess.Timed -> "Avec temps"
    AppAccess.Always -> "Toujours"
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
                    AppSetting(LaunchableApp(id = "com.example.camera", label = "Appareil photo"), AppAccess.Blocked),
                    AppSetting(LaunchableApp(id = "com.example.clock", label = "Horloge"), AppAccess.Timed),
                    AppSetting(LaunchableApp(id = "com.example.radio", label = "Radio"), AppAccess.Always),
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
