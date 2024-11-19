package com.minhdn.smartwatering.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.minhdn.smartwatering.R
import com.minhdn.smartwatering.data.firebase.FirebaseHelper
import com.minhdn.smartwatering.data.firebase.FirebaseHelper.getDatabaseReference
import com.minhdn.smartwatering.data.local.entity.HistoryEntity
import com.minhdn.smartwatering.data.local.repo.HistoryRepository
import com.minhdn.smartwatering.data.prefs.SharedPreferencesHelper
import com.minhdn.smartwatering.notification.NotificationFactory
import com.minhdn.smartwatering.notification.NotificationFactory.Companion.NOTIFICATION_CHANNEL
import com.minhdn.smartwatering.notification.NotificationType
import com.minhdn.smartwatering.presentation.MainActivity
import com.minhdn.smartwatering.utils.Constants.IS_PUMP_ON
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WateringService : Service() {

    inner class LocalBinder : Binder() {
        val service: WateringService
            get() = this@WateringService
    }

    private val iBinder: IBinder = LocalBinder()

    private var coroutineScope: CoroutineScope? = null

    private val historyRepository = HistoryRepository()

    override fun onBind(intent: Intent?): IBinder {
        return iBinder
    }

    override fun onCreate() {
        super.onCreate()
        isRunning = true
        coroutineScope = CoroutineScope(Dispatchers.IO)
        createForegroundNotification()
        regisData()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            createForegroundNotification()
            if (intent.getBooleanExtra(IS_FROM_SCHEDULE, false)) {
                startSchedule()
            }
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            START_NOT_STICKY
        } else {
            START_STICKY
        }
    }

    private fun regisData() {
        getDatabaseReference(IS_PUMP_ON).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == true) {
                    coroutineScope?.launch {
                        pushNotification()
                        addHistory()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun pushNotification() {
        NotificationFactory(this).apply {
            cancel(NotificationType.AutoWatering().notificationId)
            pushNotificationByType(
                NotificationType.AutoWatering()
            )
        }
    }

    fun addHistory() {
        coroutineScope?.launch {
            historyRepository.insertHistory(
                HistoryEntity(
                    id = 0,
                    time = System.currentTimeMillis()
                )
            )
        }
    }


    private fun createForegroundNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )
        }

        val notification =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(this.getString(R.string.app_name))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                .setShowWhen(false)
                .setOngoing(true)
                .setContentIntent(
                    PendingIntent.getActivity(
                        this,
                        0,
                        Intent(this, MainActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        },
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                )
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .build()

        notification.flags = Notification.FLAG_NO_CLEAR or Notification.FLAG_ONGOING_EVENT
        startForeground(CHANNEL_ID, notification)
    }

    private fun startSchedule() {
        val time = SharedPreferencesHelper.getInstance(this).getDurationReminder()
        Log.d("myptl", "startSchedule: $time")
        coroutineScope?.launch {
            delay(time * 60000L)
            getDatabaseReference(IS_PUMP_ON).setValue(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        coroutineScope?.cancel()
        coroutineScope = null
    }

    companion object {
        const val IS_FROM_SCHEDULE = "is_from_schedule"
        const val CHANNEL_ID = 1
        var isRunning = false

        fun start(context: Context) {
            if (!isRunning) {
                val intent = Intent(context, WateringService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ContextCompat.startForegroundService(context, intent)
                } else {
                    context.startService(intent)
                }
            }
        }
    }
}