package com.babatunde.okido.core.providers

import com.babatunde.okido.core.LaunchableApp

interface AppIconLoader {
    /** The app's icon as PNG bytes, or null when the platform has none. */
    fun icon(app: LaunchableApp): ByteArray?
}
