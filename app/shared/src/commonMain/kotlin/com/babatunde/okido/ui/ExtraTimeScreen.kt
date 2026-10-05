package com.babatunde.okido.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.babatunde.okido.core.GrantExtraTimeResult
import com.babatunde.okido.ui.components.PinPad
import com.babatunde.okido.ui.components.SegmentedControl
import com.babatunde.okido.ui.theme.OkidoColors
import com.babatunde.okido.ui.theme.OkidoTheme
import okido.shared.generated.resources.Res
import okido.shared.generated.resources.hourglass
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

private val durations = listOf(1.minutes, 15.minutes, 30.minutes)

/**
 * Locked: shows the "time's up" screen first, then the code step once the child asks for more time.
 * With [onBack] (asking from the home screen), it opens straight on the code step.
 */
@Composable
fun ExtraTimeScreen(
    title: String = "Temps écoulé",
    state: ExtraTimeUiState = ExtraTimeUiState(),
    onPinChange: (String) -> Unit = {},
    onDurationChange: (Duration) -> Unit = {},
    onGrant: () -> Unit = {},
    onBack: (() -> Unit)? = null,
    onParentSettings: (() -> Unit)? = null,
) {
    var askingCode by remember { mutableStateOf(onBack != null) }
    Surface(modifier = Modifier.fillMaxSize()) {
        if (askingCode) {
            CodeStep(
                state = state,
                onPinChange = onPinChange,
                onDurationChange = onDurationChange,
                onGrant = onGrant,
                onCancel = {
                    onPinChange("")
                    if (onBack != null) onBack() else askingCode = false
                },
            )
        } else {
            TimeUpStep(title = title, onAskMoreTime = { askingCode = true }, onParentSettings = onParentSettings)
        }
    }
}

@Composable
private fun TimeUpStep(title: String, onAskMoreTime: () -> Unit, onParentSettings: (() -> Unit)?) {
    Column(
        modifier = Modifier.fillMaxSize().safeContentPadding().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f))
        Box(
            modifier = Modifier.size(96.dp).background(OkidoColors.Indigo, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.hourglass),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(52.dp),
            )
        }
        Spacer(Modifier.height(24.dp))
        Text(title, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Tu as atteint ta limite de temps.\nDemande à un parent s'il t'en faut encore.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.weight(1f))
        Button(
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
private fun CodeStep(
    state: ExtraTimeUiState,
    onPinChange: (String) -> Unit,
    onDurationChange: (Duration) -> Unit,
    onGrant: () -> Unit,
    onCancel: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().safeContentPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        TextButton(onClick = onCancel, modifier = Modifier.align(Alignment.Start)) {
            Text("Annuler", style = MaterialTheme.typography.bodyLarge)
        }
        SegmentedControl(
            options = durations,
            selected = state.duration,
            onSelect = onDurationChange,
            label = { "${it.inWholeMinutes} min" },
            modifier = Modifier.padding(horizontal = 32.dp),
        )
        Spacer(Modifier.weight(1f))
        PinPad(
            title = "Code parent",
            subtitle = "pour accorder ${state.duration.inWholeMinutes} min de plus",
            pin = state.pin,
            onPinChange = onPinChange,
            onSubmit = onGrant,
            error = state.result == GrantExtraTimeResult.InvalidPin,
        )
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
private fun CodePreview() {
    OkidoTheme { ExtraTimeScreen(title = "Plus de temps", state = ExtraTimeUiState(pin = "12"), onBack = {}) }
}
