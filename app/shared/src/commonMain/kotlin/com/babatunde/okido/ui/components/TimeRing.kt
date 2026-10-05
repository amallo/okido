package com.babatunde.okido.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.babatunde.okido.ui.theme.OkidoColors
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

/** Minutes left, rounded up, as shown to the child: "< 1" in the last minute. */
fun remainingMinutes(remaining: Duration): String {
    val minutes = (remaining.inWholeSeconds + 59) / 60
    return if (minutes <= 1) "< 1" else "$minutes"
}

/** Remaining time as a ring that empties over [full], with the minutes left in the middle when [showLabel]. */
@Composable
fun TimeRing(
    remaining: Duration,
    modifier: Modifier = Modifier,
    full: Duration = 1.hours,
    size: Dp = 168.dp,
    strokeWidth: Dp = 14.dp,
    showLabel: Boolean = true,
) {
    val target = (remaining / full).toFloat().coerceIn(0f, 1f)
    val progress by animateFloatAsState(target)
    val track = MaterialTheme.colorScheme.surfaceVariant
    val color = if (remaining.inWholeMinutes < 5) OkidoColors.Orange else OkidoColors.Indigo
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            // Inset by half the stroke so the ring stays inside its bounds.
            val inset = stroke.width / 2
            val topLeft = Offset(inset, inset)
            val arcSize = Size(this.size.width - stroke.width, this.size.height - stroke.width)
            drawArc(track, 0f, 360f, useCenter = false, topLeft = topLeft, size = arcSize, style = stroke)
            drawArc(color, -90f, 360f * progress, useCenter = false, topLeft = topLeft, size = arcSize, style = stroke)
        }
        if (showLabel) Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = remainingMinutes(remaining),
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "min restantes",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
