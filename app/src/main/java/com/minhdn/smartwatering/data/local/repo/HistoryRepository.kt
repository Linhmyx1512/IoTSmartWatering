package com.minhdn.smartwatering.data.local.repo

import com.minhdn.smartwatering.App
import com.minhdn.smartwatering.data.local.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HistoryRepository {
    private val historyDAO = App().database.getHistoryDAO()

    suspend fun insertHistory(historyEntity: HistoryEntity): Long {
        return historyDAO.insertHistory(historyEntity)
    }

    suspend fun deleteHistory(id: Long) {
        historyDAO.deleteHistory(id)
    }

    fun getAllHistory(): Flow<List<HistoryEntity>> {
        // sort by time des
        return historyDAO.getAllHistory()
            .map { it.sortedByDescending { historyEntity -> historyEntity.time } }
    }
}