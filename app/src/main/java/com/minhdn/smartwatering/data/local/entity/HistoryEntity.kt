package com.minhdn.smartwatering.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "time")
    val time: Long
) {
    fun formatTime(): String {
        val date = Date(time)
        val calendar = Calendar.getInstance().apply { time = date }
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val suffix = when {
            day in 11..13 -> "th"
            day % 10 == 1 -> "st"
            day % 10 == 2 -> "nd"
            day % 10 == 3 -> "rd"
            else -> "th"
        }

        val formatter = SimpleDateFormat("MMM d'$suffix', yyyy • HH:mm:ss", Locale.ENGLISH)
        return formatter.format(date)
    }
}
