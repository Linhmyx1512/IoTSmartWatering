package com.minhdn.smartwatering.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.minhdn.smartwatering.broadcast.ReminderReceiver
import com.minhdn.smartwatering.utils.Constants.KEY_REMINDER_ID
import java.util.Calendar

class ReminderFactory(private val context: Context) {

    private val alarmManager by lazy { context.getSystemService(Context.ALARM_SERVICE) as AlarmManager }

    fun pushReminder(isEveryday: Boolean, hour: Int, minute: Int) {
        if (isEveryday) {
            handleReminderWeekly(hour, minute)
        } else {
            handleReminderSpecificTime(hour, minute)
        }
    }

    fun cancelReminder(isEveryday: Boolean) {
        val intent = Intent(context, ReminderReceiver::class.java)
        if (!isEveryday) {
            alarmManager.cancel(
                PendingIntent.getBroadcast(
                    context,
                    REMINDER_ID,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        } else {
            for (i in 0..6) {
                alarmManager.cancel(
                    PendingIntent.getBroadcast(
                        context,
                        REMINDER_ID + i,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                )
            }
        }
    }

    fun updateReminder(isEveryday: Boolean, hour: Int, minute: Int) {
        cancelReminder(!isEveryday)
        pushReminder(isEveryday, hour, minute)
    }

    private fun handleReminderSpecificTime(hour: Int, minute: Int) {
        val now = Calendar.getInstance()
        val currentHour = now.get(Calendar.HOUR_OF_DAY)
        val currentMinute = now.get(Calendar.MINUTE)

        if (currentHour > hour || (currentHour == hour && currentMinute > minute)) {
            return
        }

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(KEY_REMINDER_ID, REMINDER_ID)
        }

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REMINDER_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }


    private fun handleReminderWeekly(hour: Int, minute: Int) {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(KEY_REMINDER_ID, REMINDER_ID)
        }

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.WEEK_OF_YEAR, 1)
            }
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REMINDER_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY * 7,
            pendingIntent
        )
    }

    companion object {
        const val REMINDER_ID = 2211
    }
}