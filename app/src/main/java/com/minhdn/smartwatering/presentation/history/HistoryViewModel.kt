package com.minhdn.smartwatering.presentation.history

import androidx.lifecycle.ViewModel
import com.minhdn.smartwatering.data.local.repo.HistoryRepository

class HistoryViewModel : ViewModel() {
    private val repository = HistoryRepository()

    private val _histories = repository.getAllHistory()
    val histories
        get() = _histories

}