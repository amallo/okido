package com.babatunde.okido

import android.app.Application
import com.babatunde.okido.di.initKoin
import com.babatunde.okido.ui.DEBUG_PROPERTY
import org.koin.android.ext.koin.androidContext

class OkidoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@OkidoApplication)
            properties(mapOf(DEBUG_PROPERTY to BuildConfig.DEBUG))
        }
    }
}
