package com.babatunde.okido.infra

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.babatunde.okido.core.EndExtraTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ExtraTimeEndedReceiver : BroadcastReceiver(), KoinComponent {
    private val endExtraTime: EndExtraTime by inject()

    override fun onReceive(context: Context, intent: Intent) {
        endExtraTime()
    }
}
