package com.babatunde.okido.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.babatunde.okido.ui.theme.OkidoTheme

const val PIN_LENGTH = 4

private val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "⌫")

/**
 * iOS-style passcode entry: a row of dots and a round numeric keypad.
 * Submits on its own once [PIN_LENGTH] digits are typed, and shakes the dots when [error] turns true.
 */
@Composable
fun PinPad(
    title: String,
    pin: String,
    onPinChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    error: Boolean = false,
    subtitle: String? = null,
) {
    val shake = remember { Animatable(0f) }
    val haptics = LocalHapticFeedback.current
    LaunchedEffect(error) {
        if (!error) return@LaunchedEffect
        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
        shake.animateTo(0f, keyframes {
            durationMillis = 400
            -24f at 50
            24f at 120
            -16f at 190
            16f at 260
            -8f at 330
        })
    }

    fun onKey(key: String) {
        when {
            key == "⌫" -> onPinChange(pin.dropLast(1))
            pin.length < PIN_LENGTH -> {
                val next = pin + key
                onPinChange(next)
                if (next.length == PIN_LENGTH) onSubmit()
            }
        }
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text(
            text = if (error) "Code incorrect" else subtitle.orEmpty(),
            style = MaterialTheme.typography.bodyMedium,
            color = if (error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.offset(x = shake.value.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            repeat(PIN_LENGTH) { index -> PinDot(filled = index < pin.length) }
        }
        Spacer(Modifier.height(40.dp))
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            keys.chunked(3).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    row.forEach { key -> PinKey(key, onClick = { onKey(key) }) }
                }
            }
        }
    }
}

@Composable
private fun PinDot(filled: Boolean) {
    val color = MaterialTheme.colorScheme.onSurface
    Box(
        modifier = Modifier
            .size(14.dp)
            .clip(CircleShape)
            .border(1.5.dp, color, CircleShape)
            .then(if (filled) Modifier.background(color) else Modifier),
    )
}

@Composable
private fun PinKey(key: String, onClick: () -> Unit) {
    val isDigit = key.isNotEmpty() && key != "⌫"
    Box(
        modifier = Modifier
            .size(78.dp)
            .clip(CircleShape)
            .then(if (isDigit) Modifier.background(MaterialTheme.colorScheme.surfaceVariant) else Modifier)
            .then(if (key.isNotEmpty()) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = key,
            fontSize = if (isDigit) 32.sp else 24.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
@Preview
private fun PinPadPreview() {
    OkidoTheme {
        PinPad(title = "Saisir le code parent", pin = "12", onPinChange = {}, onSubmit = {})
    }
}
