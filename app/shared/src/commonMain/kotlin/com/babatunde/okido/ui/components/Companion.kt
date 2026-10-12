package com.babatunde.okido.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.util.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.babatunde.okido.ui.theme.OkidoColors
import com.babatunde.okido.ui.theme.OkidoTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sin

/**
 * The device's companion: the "O" of the logo with a face, filling with sand as it gets sleepy, like the sandman's.
 * Its sand rises with [sleepiness], the share of the granted time already used, so it is full at the limit whatever the limit.
 * Awake, it is calm, breathes and blinks; once full, it falls peacefully asleep. Its mood never depends on screen time,
 * and the motion stays slow so it never calls the child.
 */
@Composable
fun Companion(sleepiness: Float, modifier: Modifier = Modifier) {
    val asleep = sleepiness >= 1f
    // The state only ticks every 30 s, so the sand glides to its new level.
    val sand by animateFloatAsState(sleepiness.coerceIn(0f, 1f), tween(1500), label = "sand")
    val transition = rememberInfiniteTransition(label = "companion")
    val breath by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(if (asleep) 3000 else 2000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "breath",
    )
    val cycle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(if (asleep) 6000 else 5000, easing = LinearEasing)),
        label = "cycle",
    )
    Canvas(modifier) {
        // Drawn in a 200 × 200 box centred on the origin, like the sketches.
        withTransform({
            translate(center.x, center.y)
            scale(size.minDimension / 200f, size.minDimension / 200f, pivot = Offset.Zero)
        }) {
            if (asleep) {
                scale(1f + 0.02f * breath, 1f - 0.02f * breath, pivot = Offset(0f, 82f)) {
                    asleepBody(sandLevel = sandLevel(sand))
                }
                sleepyZ(phase = cycle * 2 % 1f, factor = 1f)
                sleepyZ(phase = (cycle * 2 + 0.5f) % 1f, factor = 0.75f)
            } else {
                scale(1f + 0.03f * breath, 1f - 0.03f * breath, pivot = Offset(0f, 82f)) {
                    awakeBody(sandLevel = sandLevel(sand), eyeOpening = eyeOpening(cycle))
                }
            }
        }
    }
}

/** From a thin layer at the bottom of the ring (awake) to almost the eyes (asleep). */
private fun sandLevel(sleepiness: Float): Float = lerp(50f, 4f, sleepiness)

/** Eyes shut briefly near the end of each cycle. */
private fun eyeOpening(cycle: Float): Float = when {
    cycle < 0.92f -> 1f
    cycle < 0.95f -> 1f - (cycle - 0.92f) / 0.03f * 0.9f
    cycle < 0.98f -> 0.1f + (cycle - 0.95f) / 0.03f * 0.9f
    else -> 1f
}

private val Ink = OkidoColors.Indigo
private val Sand = OkidoColors.Orange
private val Line = Stroke(width = 6f, cap = StrokeCap.Round)

/** The ring, with sand up to [sandLevel] (y, downwards). */
private fun DrawScope.ring(sandLevel: Float) {
    clipPath(pinchedRing(58f)) {
        drawPath(
            Path().apply {
                moveTo(-60f, sandLevel)
                quadraticTo(0f, sandLevel - 10f, 60f, sandLevel)
                lineTo(60f, 70f)
                lineTo(-60f, 70f)
                close()
            },
            Sand,
        )
    }
    drawPath(pinchedRing(70f), Ink, style = Stroke(width = 24f, join = StrokeJoin.Round))
}

/** How far the sides come in at the waist, as a share of the radius. */
private const val Pinch = 0.22f

/** A circle whose sides come in gently at mid-height, like an hourglass. */
private fun pinchedRing(radius: Float) = Path().apply {
    val steps = 96
    for (i in 0 until steps) {
        val angle = 2 * PI * i / steps
        val waist = 1f - Pinch * exp(-(sin(angle) / 0.4).pow(2)).toFloat()
        val x = radius * cos(angle).toFloat() * waist
        val y = radius * sin(angle).toFloat()
        if (i == 0) moveTo(x, y) else lineTo(x, y)
    }
    close()
}

/** Calm: open eyes and a neutral mouth, so the screen never looks like what makes it happy. */
private fun DrawScope.awakeBody(sandLevel: Float, eyeOpening: Float) {
    ring(sandLevel)
    scale(1f, eyeOpening, pivot = Offset(0f, -14f)) {
        for (x in listOf(-20f, 20f)) {
            drawCircle(Ink, radius = 9f, center = Offset(x, -14f))
            drawCircle(Color.White, radius = 3f, center = Offset(x + 3f, -17f))
        }
    }
    drawPath(curve(-12f, 12f, 0f, 12f, 12f, 12f), Ink, style = Line)
}

/** Peacefully asleep: closed eyes and a small smile, never worn out by the screen. */
private fun DrawScope.asleepBody(sandLevel: Float) {
    ring(sandLevel)
    drawPath(curve(-29f, -10f, -20f, -3f, -11f, -10f), Ink, style = Line)
    drawPath(curve(11f, -10f, 20f, -3f, 29f, -10f), Ink, style = Line)
    drawPath(curve(-10f, 12f, 0f, 20f, 10f, 12f), Ink, style = Line)
}

/** A "z" drifting up and away from the top right, fading in then out over [phase]. */
private fun DrawScope.sleepyZ(phase: Float, factor: Float) {
    val alpha = if (phase < 0.3f) phase / 0.3f else 1f - (phase - 0.3f) / 0.7f
    translate(60f + 12f * phase, -80f - 16f * phase) {
        scale(factor, pivot = Offset.Zero) {
            drawPath(
                Path().apply {
                    moveTo(0f, 0f)
                    lineTo(12f, 0f)
                    lineTo(0f, 14f)
                    lineTo(12f, 14f)
                },
                Sand,
                alpha = alpha,
                style = Stroke(width = 4.5f, cap = StrokeCap.Round, join = StrokeJoin.Round),
            )
        }
    }
}

private fun curve(x0: Float, y0: Float, cx: Float, cy: Float, x1: Float, y1: Float) = Path().apply {
    moveTo(x0, y0)
    quadraticTo(cx, cy, x1, y1)
}

@Composable
@Preview
private fun CompanionPreview() {
    OkidoTheme {
        Row {
            Companion(sleepiness = 0f, modifier = Modifier.size(120.dp))
            Companion(sleepiness = 0.6f, modifier = Modifier.size(120.dp))
            Companion(sleepiness = 1f, modifier = Modifier.size(120.dp))
        }
    }
}
