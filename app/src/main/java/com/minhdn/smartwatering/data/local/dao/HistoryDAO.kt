package com.minhdn.smartwatering.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.minhdn.smartwatering.data.local.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface HistoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(historyEntity: HistoryEntity): Long

    @Query("DELETE FROM HistoryEntity WHERE id = :id")
    suspend fun deleteHistory(id: Long)

    @Query("SELECT * FROM HistoryEntity")
    fun getAllHistory(): Flow<List<HistoryEntity>>
}