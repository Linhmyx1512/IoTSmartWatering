package com.minhdn.smartwatering.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.minhdn.smartwatering.App
import com.minhdn.smartwatering.data.local.dao.HistoryDAO
import com.minhdn.smartwatering.data.local.entity.HistoryEntity


@Database(entities = [HistoryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getHistoryDAO(): HistoryDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getInstance(): AppDatabase = synchronized(this) {
            return INSTANCE ?: kotlin.run {
                Room.databaseBuilder(
                    App.instance,
                    AppDatabase::class.java,
                    "smart_watering.db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}