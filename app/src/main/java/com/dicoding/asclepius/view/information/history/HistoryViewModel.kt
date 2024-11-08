package com.dicoding.asclepius.view.information.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.data.local.repository.HistoryRepo
import com.dicoding.asclepius.helper.AppExecutors
import kotlinx.coroutines.runBlocking

class HistoryViewModel(private val eventRepo: HistoryRepo, private val appExecutors: AppExecutors) : ViewModel()   {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _favoriteEventsEntity = MutableLiveData<List<HistoryEntity>>()
    val favoriteEventsEntity: LiveData<List<HistoryEntity>> = _favoriteEventsEntity

    init {
        observeHistory()
    }

    private fun observeHistory() {
        _isLoading.value = true
        eventRepo.getHistory().observeForever { events ->
            _isLoading.value = false
            _favoriteEventsEntity.value = events
        }
    }
}