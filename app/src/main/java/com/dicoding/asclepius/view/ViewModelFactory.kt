package com.dicoding.asclepius.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.local.repository.HistoryRepo
import com.dicoding.asclepius.data.local.room.HistoryDatabase
import com.dicoding.asclepius.helper.AppExecutors
import com.dicoding.asclepius.view.information.history.HistoryViewModel

class ViewModelFactory private constructor(private val historyRepo: HistoryRepo) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> historyViewModel as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
    val appExecutors = AppExecutors()
    private val historyViewModel = HistoryViewModel(historyRepo, appExecutors)

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(HistoryRepo.getInstance(HistoryDatabase.getInstance(context).historyDao()))
            }.also { instance = it }
    }
}