package com.babatunde.okido.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.babatunde.okido.core.GrantExtraTimeResult
import com.babatunde.okido.core.LaunchableApp
import com.babatunde.okido.ui.components.Companion
import com.babatunde.okido.ui.components.ChildErrorMessage
import com.babatunde.okido.ui.components.ChildHint
import com.babatunde.okido.ui.components.PinPad
import com.babatunde.okido.ui.theme.OkidoTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

/** A granted duration as shown to the parent, e.g. "15 min" or "1 h". */
private fun Duration.label(): String =
    if (this >= 1.hours && inWholeMinutes % 60 == 0L) "$inWholeHours h" else "$inWholeMinutes min"

/**
 * Locked: shows the "time's up" screen first, then, once the child asks for more time,
 * a parent enters their code on one screen and picks the duration on the next.
 * The time's up screen lists the [alwaysApps], which stay usable without granted time.
 * With [onBack] (asking from the home screen), it opens straight on the code screen.
 */
@Composable
fun ExtraTimeScreen(
    title: String = "Temps écoulé",
    state: ExtraTimeUiState = ExtraTimeUiState(),
    alwaysApps: List<LaunchableApp> = emptyList(),
    icons: Map<String, ImageBitmap> = emptyMap(),
    onLaunch: (LaunchableApp) -> Unit = {},
    onPinChange: (String) -> Unit = {},
    onSubmitPin: () -> Unit = {},
    onGrant: (Duration) -> Unit = {},
    onCancel: () -> Unit = {},
    onBack: (() -> Unit)? = null,
    onParentSettings: (() -> Unit)? = null,
) {
    var askingCode by remember { mutableStateOf(onBack != null) }
    Surface(modifier = Modifier.fillMaxSize()) {
        val cancel = {
            onCancel()
            if (onBack != null) onBack() else askingCode = false
        }
        if (askingCode && state.pinAccepted) {
            DurationStep(durations = state.durations, onGrant = onGrant, onCancel = cancel)
        } else if (askingCode) {
            CodeStep(state = state, onPinChange = onPinChange, onSubmitPin = onSubmitPin, onCancel = cancel)
        } else {
            TimeUpStep(
                title = title,
                alwaysApps = alwaysApps,
                icons = icons,
                onLaunch = onLaunch,
                onAskMoreTime = { askingCode = true },
                onParentSettings = onParentSettings,
            )
        }
    }
}

@Composable
private fun TimeUpStep(
    title: String,
    alwaysApps: List<LaunchableApp>,
    icons: Map<String, ImageBitmap>,
    onLaunch: (LaunchableApp) -> Unit,
    onAskMoreTime: () -> Unit,
    onParentSettings: (() -> Unit)?,
) {
    Column(
        modifier = Modifier.fillMaxSize().safeContentPadding().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f))
        Companion(sleepiness = 1f, modifier = Modifier.size(140.dp))
        Spacer(Modifier.height(24.dp))
        Text(title, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "C'est l'heure de ma sieste.\nEt toi, tu fais quoi ?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        if (alwaysApps.isNotEmpty()) {
            Spacer(Modifier.height(40.dp))
            AlwaysAvailableApps(alwaysApps, icons, onLaunch)
        }
        Spacer(Modifier.weight(1f))
        // Secondary on purpose: the screen's message is the pause, not asking for more.
        OutlinedButton(
            onClick = onAskMoreTime,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp),
        ) {
            Text("Demander plus de temps", style = MaterialTheme.typography.titleMedium)
        }
        if (onParentSettings != null) {
            TextButton(onClick = onParentSettings, modifier = Modifier.padding(top = 8.dp)) {
                Text("Réglages parent", style = MaterialTheme.typography.bodyLarge)
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun AlwaysAvailableApps(
    apps: List<LaunchableApp>,
    icons: Map<String, ImageBitmap>,
    onLaunch: (LaunchableApp) -> Unit,
) {
    Text(
        text = "TOUJOURS DISPONIBLES",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Spacer(Modifier.height(12.dp))
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    ) {
        items(apps, key = { it.id }) { app ->
            AppTile(app, icons[app.id], onClick = { onLaunch(app) }, modifier = Modifier.width(76.dp))
        }
    }
}

@Composable
private fun CodeStep(
    state: ExtraTimeUiState,
    onPinChange: (String) -> Unit,
    onSubmitPin: () -> Unit,
    onCancel: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextButton(onClick = onCancel, modifier = Modifier.align(Alignment.Start)) {
            Text("Annuler", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(Modifier.weight(1f))
        PinPad(
            title = "Code parent",
            subtitle = "pour accorder plus de temps",
            pin = state.pin,
            onPinChange = onPinChange,
            onSubmit = onSubmitPin,
            error = state.result == GrantExtraTimeResult.InvalidPin,
            errorMessage = ChildErrorMessage,
            hint = ChildHint,
        )
        Spacer(Modifier.weight(1f))
    }
}

/** One large button per duration: a single tap grants it. */
@Composable
private fun DurationStep(
    durations: List<Duration>,
    onGrant: (Duration) -> Unit,
    onCancel: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextButton(onClick = onCancel, modifier = Modifier.align(Alignment.Start)) {
            Text("Annuler", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(Modifier.weight(1f))
        Text("Combien de temps ?", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "à accorder en plus",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(32.dp))
        Column(
            modifier = Modifier.padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            durations.forEach { duration ->
                OutlinedButton(
                    onClick = { onGrant(duration) },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                ) {
                    Text(duration.label(), style = MaterialTheme.typography.titleLarge)
                }
            }
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
@Preview
private fun TimeUpPreview() {
    OkidoTheme { ExtraTimeScreen(onParentSettings = {}) }
}

@Composable
@Preview
private fun TimeUpWithAlwaysAppsPreview() {
    val alwaysApps = listOf(
        LaunchableApp(id = "fr.radiofrance.app", label = "Radio"),
        LaunchableApp(id = "com.android.deskclock", label = "Horloge"),
    )
    OkidoTheme { ExtraTimeScreen(alwaysApps = alwaysApps, onParentSettings = {}) }
}

@Composable
@Preview
private fun CodePreview() {
    OkidoTheme { ExtraTimeScreen(title = "Plus de temps", state = ExtraTimeUiState(pin = "12"), onBack = {}) }
}

@Composable
@Preview
private fun DurationPreview() {
    OkidoTheme { ExtraTimeScreen(title = "Plus de temps", state = ExtraTimeUiState(pinAccepted = true), onBack = {}) }
}
