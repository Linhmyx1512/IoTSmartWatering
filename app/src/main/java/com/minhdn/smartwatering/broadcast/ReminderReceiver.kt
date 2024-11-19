package com.minhdn.smartwatering.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.minhdn.smartwatering.data.firebase.FirebaseHelper
import com.minhdn.smartwatering.notification.NotificationFactory
import com.minhdn.smartwatering.notification.NotificationType
import com.minhdn.smartwatering.service.WateringService
import com.minhdn.smartwatering.service.WateringService.Companion.IS_FROM_SCHEDULE
import com.minhdn.smartwatering.utils.Constants.IS_PUMP_ON
import com.minhdn.smartwatering.utils.Constants.KEY_REMINDER_ID

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return

        val reminderId = intent.getIntExtra(KEY_REMINDER_ID, -1)
        if (reminderId != -1) {
            NotificationFactory(context).pushNotificationByType(NotificationType.Reminder())

            FirebaseHelper.getDatabaseReference(IS_PUMP_ON).setValue(true)

            context.startService(Intent(context, WateringService::class.java).apply {
                putExtra(IS_FROM_SCHEDULE, true)
            })
        }
    }
}