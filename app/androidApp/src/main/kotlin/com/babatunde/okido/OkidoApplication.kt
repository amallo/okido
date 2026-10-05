package com.babatunde.okido

import android.app.Application
import com.babatunde.okido.di.initKoin
import org.koin.android.ext.koin.androidContext

class OkidoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@OkidoApplication)
        }
    }
}
