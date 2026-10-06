package com.babatunde.okido.infra.providers

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import com.babatunde.okido.core.LaunchableApp
import com.babatunde.okido.core.providers.AppIconLoader
import java.io.ByteArrayOutputStream

class PackageManagerAppIconLoader(private val context: Context) : AppIconLoader {
    override fun icon(app: LaunchableApp): ByteArray? {
        val drawable = try {
            context.packageManager.getApplicationIcon(app.id)
        } catch (_: PackageManager.NameNotFoundException) {
            return null
        }
        val bitmap = Bitmap.createBitmap(ICON_SIZE_PX, ICON_SIZE_PX, Bitmap.Config.ARGB_8888)
        drawable.setBounds(0, 0, ICON_SIZE_PX, ICON_SIZE_PX)
        drawable.draw(Canvas(bitmap))
        return ByteArrayOutputStream().use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.toByteArray()
        }
    }

    private companion object {
        const val ICON_SIZE_PX = 144
    }
}
