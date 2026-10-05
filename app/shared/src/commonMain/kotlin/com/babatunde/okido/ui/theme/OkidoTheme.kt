package com.babatunde.okido.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// iOS system colors, as used by Screen Time.
object OkidoColors {
    val Blue = Color(0xFF007AFF)
    val Green = Color(0xFF34C759)
    val Red = Color(0xFFFF3B30)
    val Orange = Color(0xFFFF9500)
    val Indigo = Color(0xFF5856D6)
    val SecondaryLabel = Color(0xFF8E8E93)
}

private val LightColors = lightColorScheme(
    primary = OkidoColors.Blue,
    onPrimary = Color.White,
    secondary = OkidoColors.Indigo,
    error = OkidoColors.Red,
    background = Color(0xFFF2F2F7),
    onBackground = Color.Black,
    surface = Color(0xFFF2F2F7),
    onSurface = Color.Black,
    surfaceContainer = Color.White,
    surfaceVariant = Color(0xFFE5E5EA),
    onSurfaceVariant = OkidoColors.SecondaryLabel,
    outlineVariant = Color(0xFFC6C6C8),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF0A84FF),
    onPrimary = Color.White,
    secondary = Color(0xFF5E5CE6),
    error = Color(0xFFFF453A),
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    surfaceContainer = Color(0xFF1C1C1E),
    surfaceVariant = Color(0xFF2C2C2E),
    onSurfaceVariant = OkidoColors.SecondaryLabel,
    outlineVariant = Color(0xFF38383A),
)

private val OkidoTypography = Typography().run {
    copy(
        headlineLarge = TextStyle(fontSize = 34.sp, fontWeight = FontWeight.Bold, lineHeight = 41.sp),
        headlineMedium = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, lineHeight = 34.sp),
        titleLarge = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold, lineHeight = 25.sp),
        titleMedium = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.SemiBold, lineHeight = 22.sp),
        bodyLarge = TextStyle(fontSize = 17.sp, lineHeight = 22.sp),
        bodyMedium = TextStyle(fontSize = 15.sp, lineHeight = 20.sp),
        labelSmall = TextStyle(fontSize = 13.sp, lineHeight = 18.sp, letterSpacing = 0.5.sp),
    )
}

@Composable
fun OkidoTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = OkidoTypography,
        content = content,
    )
}
