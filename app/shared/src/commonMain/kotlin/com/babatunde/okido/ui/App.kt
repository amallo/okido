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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.babatunde.okido.core.GrantExtraTimeResult
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

private val durations = listOf(1.minutes, 15.minutes, 30.minutes)

@Composable
fun App(viewModel: ExtraTimeViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ExtraTimeScreen(
        state = state,
        onPinChange = viewModel::onPinChange,
        onDurationChange = viewModel::onDurationChange,
        onGrant = viewModel::onGrant,
    )
}

@Composable
@Preview
fun ExtraTimeScreen(
    state: ExtraTimeUiState = ExtraTimeUiState(),
    onPinChange: (String) -> Unit = {},
    onDurationChange: (Duration) -> Unit = {},
    onGrant: () -> Unit = {},
) {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.safeContentPadding().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text("Temps supplémentaire", style = MaterialTheme.typography.headlineSmall)
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
                when (state.result) {
                    GrantExtraTimeResult.Granted -> Text("Temps accordé")
                    GrantExtraTimeResult.InvalidPin ->
                        Text("PIN incorrect", color = MaterialTheme.colorScheme.error)
                    null -> {}
                }
            }
        }
    }
}
