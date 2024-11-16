package com.minhdn.smartwatering.data.local.repo

import com.minhdn.smartwatering.App
import com.minhdn.smartwatering.data.local.entity.HistoryEntity

class HistoryRepository {
    private val historyDAO = App().database.getHistoryDAO()

    suspend fun insertHistory(historyEntity: HistoryEntity): Long {
        return historyDAO.insertHistory(historyEntity)
    }

    suspend fun deleteHistory(id: Long) {
        historyDAO.deleteHistory(id)
    }
}