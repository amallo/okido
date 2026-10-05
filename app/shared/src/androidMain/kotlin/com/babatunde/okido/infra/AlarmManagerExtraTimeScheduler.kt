package com.babatunde.okido.infra

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import com.babatunde.okido.core.ExtraTimeScheduler
import kotlin.time.Duration

class AlarmManagerExtraTimeScheduler(private val context: Context) : ExtraTimeScheduler {
    override fun scheduleEnd(after: Duration) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        val triggerAt = SystemClock.elapsedRealtime() + after.inWholeMilliseconds
        val intent = PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, ExtraTimeEndedReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAt, intent)
        } else {
            alarmManager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAt, intent)
        }
    }
}
