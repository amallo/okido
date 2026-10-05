package com.babatunde.okido.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.babatunde.okido.core.GrantExtraTimeResult
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

private val durations = listOf(1.minutes, 15.minutes, 30.minutes)

@Composable
@Preview
fun ExtraTimeScreen(
    title: String = "Temps écoulé",
    state: ExtraTimeUiState = ExtraTimeUiState(),
    onPinChange: (String) -> Unit = {},
    onDurationChange: (Duration) -> Unit = {},
    onGrant: () -> Unit = {},
    onBack: (() -> Unit)? = null,
    onParentSettings: (() -> Unit)? = null,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.safeContentPadding().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(title, style = MaterialTheme.typography.headlineSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                durations.forEach { duration ->
                    FilterChip(
                        selected = duration == state.duration,
                        onClick = { onDurationChange(duration) },
                        label = { Text("${duration.inWholeMinutes} min") },
                    )
                }
            }
            OutlinedTextField(
                value = state.pin,
                onValueChange = onPinChange,
                label = { Text("PIN parent") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            )
            Button(onClick = onGrant, enabled = state.pin.isNotEmpty()) {
                Text("Accorder")
            }
            if (state.result == GrantExtraTimeResult.InvalidPin) {
                Text("PIN incorrect", color = MaterialTheme.colorScheme.error)
            }
            if (onBack != null) {
                TextButton(onClick = onBack) {
                    Text("Retour")
                }
            }
            if (onParentSettings != null) {
                TextButton(onClick = onParentSettings) {
                    Text("Réglages parent")
                }
            }
        }
    }
}
