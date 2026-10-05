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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.babatunde.okido.ui.theme.OkidoColors
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

/** Remaining time as a ring that empties over [full], with the minutes left in the middle. */
@Composable
fun TimeRing(
    remaining: Duration,
    modifier: Modifier = Modifier,
    full: Duration = 1.hours,
    size: Dp = 168.dp,
) {
    val target = (remaining / full).toFloat().coerceIn(0f, 1f)
    val progress by animateFloatAsState(target)
    val track = MaterialTheme.colorScheme.surfaceVariant
    val color = if (remaining.inWholeMinutes < 5) OkidoColors.Orange else OkidoColors.Indigo
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
            drawArc(track, startAngle = 0f, sweepAngle = 360f, useCenter = false, style = stroke)
            drawArc(color, startAngle = -90f, sweepAngle = 360f * progress, useCenter = false, style = stroke)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val minutes = (remaining.inWholeSeconds + 59) / 60
            Text(
                text = if (minutes <= 1) "< 1" else "$minutes",
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
