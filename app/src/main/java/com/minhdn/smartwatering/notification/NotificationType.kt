package com.minhdn.smartwatering.notification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class NotificationType(
    open val notificationId: Int
) : Parcelable {

    @Parcelize
    class AutoWatering : NotificationType(AUTO_WATERING_NOTIFICATION_ID) {
        companion object {
            const val AUTO_WATERING_NOTIFICATION_ID = 15001
        }
    }
}