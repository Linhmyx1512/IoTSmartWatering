package com.minhdn.smartwatering.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.minhdn.smartwatering.data.firebase.FirebaseHelper
import com.minhdn.smartwatering.data.prefs.SharedPreferencesHelper
import com.minhdn.smartwatering.notification.NotificationFactory
import com.minhdn.smartwatering.notification.NotificationType
import com.minhdn.smartwatering.utils.Constants.IS_PUMP_ON
import com.minhdn.smartwatering.utils.Constants.KEY_REMINDER_ID

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return

        val reminderId = intent.getIntExtra(KEY_REMINDER_ID, -1)
        if (reminderId != -1) {
            NotificationFactory(context).pushNotificationByType(NotificationType.Reminder())

            var durationWatering =
                SharedPreferencesHelper.getInstance(context).getDurationReminder() * 60

            FirebaseHelper.getDatabaseReference(IS_PUMP_ON).setValue(true)

            while (durationWatering > 0) {
                Log.d("myptl", "durationWatering: $durationWatering")
                Handler(Looper.getMainLooper()).postDelayed({
                }, 1000L)
                durationWatering--
            }
            FirebaseHelper.getDatabaseReference(IS_PUMP_ON).setValue(false)
        }
    }
}