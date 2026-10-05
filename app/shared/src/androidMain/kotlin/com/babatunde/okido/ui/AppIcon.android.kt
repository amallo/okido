package com.babatunde.okido.ui

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.babatunde.okido.core.LaunchableApp

private const val ICON_SIZE_PX = 144

@Composable
actual fun AppIcon(app: LaunchableApp, modifier: Modifier) {
    val packageManager = LocalContext.current.packageManager
    val icon = remember(app.id) { loadIcon(packageManager, app.id) }
    if (icon == null) {
        AppIconPlaceholder(app, modifier)
    } else {
        Image(bitmap = icon, contentDescription = null, modifier = modifier.clip(AppIconShape))
    }
}

private fun loadIcon(packageManager: PackageManager, packageName: String): ImageBitmap? = try {
    val drawable = packageManager.getApplicationIcon(packageName)
    val bitmap = Bitmap.createBitmap(ICON_SIZE_PX, ICON_SIZE_PX, Bitmap.Config.ARGB_8888)
    drawable.setBounds(0, 0, ICON_SIZE_PX, ICON_SIZE_PX)
    drawable.draw(Canvas(bitmap))
    bitmap.asImageBitmap()
} catch (_: PackageManager.NameNotFoundException) {
    null
}
