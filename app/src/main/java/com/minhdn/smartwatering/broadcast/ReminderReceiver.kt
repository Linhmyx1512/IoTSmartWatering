package com.minhdn.smartwatering.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.minhdn.smartwatering.notification.NotificationFactory
import com.minhdn.smartwatering.notification.NotificationType
import com.minhdn.smartwatering.utils.Constants.KEY_REMINDER_ID

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return

        Log.d("myptl", "pushNotificationByType: ReminderReceiver")
        val reminderId = intent.getIntExtra(KEY_REMINDER_ID, -1)
        if (reminderId != -1) {
            NotificationFactory(context).pushNotificationByType(NotificationType.Reminder())
        }
    }

}