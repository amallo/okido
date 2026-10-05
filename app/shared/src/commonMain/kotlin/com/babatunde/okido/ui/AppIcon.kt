package com.babatunde.okido.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.babatunde.okido.core.LaunchableApp
import com.babatunde.okido.ui.theme.OkidoColors
import kotlin.math.absoluteValue

val AppIconShape = RoundedCornerShape(percent = 22)

private val placeholderColors = listOf(
    OkidoColors.Blue, OkidoColors.Green, OkidoColors.Orange, OkidoColors.Indigo, OkidoColors.Red,
)

/** The app's own icon, as the platform draws it. */
@Composable
expect fun AppIcon(app: LaunchableApp, modifier: Modifier = Modifier)

/** A colored rounded square with the app's initial, for when the platform has no icon to give. */
@Composable
fun AppIconPlaceholder(app: LaunchableApp, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(AppIconShape)
            .background(placeholderColors[app.id.hashCode().absoluteValue % placeholderColors.size]),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = app.label.take(1).uppercase(),
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
