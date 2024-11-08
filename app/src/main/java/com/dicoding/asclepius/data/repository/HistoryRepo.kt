package com.dicoding.asclepius.data.repository

import com.dicoding.asclepius.data.entity.HistoryEntity
import com.dicoding.asclepius.data.room.HistoryDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class HistoryRepo  private constructor(
    private val historyDao: HistoryDao,
) {
    companion object {
        @Volatile
        private var instance: HistoryRepo? = null

        fun getInstance(historyDao: HistoryDao): HistoryRepo =
            instance ?: synchronized(this) {
                instance ?: HistoryRepo(historyDao).also { instance = it }
            }
    }

    // MediatorLiveData digunakan untuk menggabungkan dua sumber data (LiveData) yang berbeda
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // insert history
    suspend fun insertHistory(history: HistoryEntity) {
        historyDao.insertHistory(history)
    }

    fun saveHistoryToDatabase(history: List<HistoryEntity>) {
        coroutineScope.launch {
            history.forEach { insertHistory(it) }
        }
    }
}