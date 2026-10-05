package com.babatunde.okido.infra

import android.content.Context
import android.content.Intent
import com.babatunde.okido.core.ScreenLocker

class HomeScreenLocker(private val context: Context) : ScreenLocker {
    override fun lock() {
        val home = Intent(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_HOME)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(home)
    }
}
