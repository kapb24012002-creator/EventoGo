package com.eventogo.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.*

object ReminderHelper {

    fun scheduleReminder(context: Context, dateStr: String, title: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            context, 
            System.currentTimeMillis().toInt(), 
            intent, 
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        try {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = format.parse(dateStr)
            date?.let {
                val calendar = Calendar.getInstance().apply {
                    time = it
                    set(Calendar.HOUR_OF_DAY, 9) // Recordatorio a las 9 AM
                    set(Calendar.MINUTE, 0)
                }

                if (calendar.timeInMillis > System.currentTimeMillis()) {
                    try {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent
                        )
                    } catch (se: SecurityException) {
                        // En versiones modernas, si no tiene permiso de alarmas exactas, usamos set
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}