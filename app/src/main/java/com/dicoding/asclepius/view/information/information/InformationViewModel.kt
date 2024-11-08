package com.dicoding.asclepius.view.information.information

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.data.response.NewsResponse
import com.dicoding.asclepius.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InformationViewModel : ViewModel(){
private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listEventsItem = MutableLiveData<List<ArticlesItem>?>()
    val listEventsItem: LiveData<List<ArticlesItem>?> = _listEventsItem

    suspend fun showEvents() {
        _isLoading.value = true
        try {
            val response = ApiConfig.getApiService().getNews(BuildConfig.API_KEY)
            _listEventsItem.value = response.articles
            Log.e("InformationViewModel", "sjuccess")
        } catch (e: Exception) {
            Log.e("InformationViewModel", "Error fetching news", e)
            _listEventsItem.value = null
        } finally {
            _isLoading.value = false
        }
    }
}