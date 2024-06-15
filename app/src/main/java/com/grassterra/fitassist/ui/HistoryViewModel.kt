package com.grassterra.fitassist.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grassterra.fitassist.database.history.HistoryItem
import com.grassterra.fitassist.repository.HistoryItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel(application: Application): ViewModel() {
    private val _historyList = MutableLiveData<List<HistoryItem>>()
    val historyList: LiveData<List<HistoryItem>> = _historyList

    private val historyItemRepository = HistoryItemRepository(application)

    init {
        viewModelScope.launch {
            getAllHistory()
        }
    }

    private fun getAllHistory(){
        viewModelScope.launch(Dispatchers.IO) {
            val itemList = historyItemRepository.getAll()
            withContext(Dispatchers.Main) {
                _historyList.value = itemList
            }
        }
    }
}