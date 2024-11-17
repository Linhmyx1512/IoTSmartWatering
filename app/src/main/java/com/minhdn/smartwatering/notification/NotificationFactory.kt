package com.minhdn.smartwatering.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.minhdn.smartwatering.R
import com.minhdn.smartwatering.presentation.MainActivity

class NotificationFactory(private val context: Context) {

    private inline val notificationManager
        get() = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    fun cancel(id: Int) {
        notificationManager.cancel(id)
    }

    fun pushNotificationByType(notificationType: NotificationType) {
        when (notificationType) {
            is NotificationType.AutoWatering -> {
                createStartPumpNotification(notificationType)
            }
        }
    }

    private fun createStartPumpNotification(notificationType: NotificationType) {
        val remoteView = RemoteViews(context.packageName, R.layout.layout_notification)

        remoteView.apply {
            setTextViewText(R.id.tvTitle, context.getString(R.string.title_start_watering))
            setTextViewText(
                R.id.tvContent,
                context.getString(R.string.content_start_watering)
            )
        }

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.app_name))
            .setCustomContentView(remoteView)
            .setCustomHeadsUpContentView(remoteView)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setShowWhen(false)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    },
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())

        createNotificationChannelIfNeed(notification)

        notificationManager.notify(
            notificationType.notificationId,
            notification.build()
        )
    }

    private fun createNotificationChannelIfNeed(notification: NotificationCompat.Builder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL = "NOTIFICATION_CHANNEL"
    }
}