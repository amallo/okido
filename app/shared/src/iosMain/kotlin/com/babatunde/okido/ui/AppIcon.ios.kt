package com.babatunde.okido.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.babatunde.okido.core.LaunchableApp

@Composable
actual fun AppIcon(app: LaunchableApp, modifier: Modifier) {
    AppIconPlaceholder(app, modifier)
}
