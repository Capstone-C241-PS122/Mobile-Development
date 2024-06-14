package com.grassterra.fitassist.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grassterra.fitassist.database.history.HistoryItem
import com.grassterra.fitassist.helper.LabelRequest
import com.grassterra.fitassist.helper.Resource
import com.grassterra.fitassist.repository.ApiRepository
import com.grassterra.fitassist.repository.HistoryItemRepository
import com.grassterra.fitassist.response.LabelPostResponse
import com.grassterra.fitassist.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailExerciseViewModel(application: Application): ViewModel() {

    private val apiRepository = ApiRepository(ApiConfig.getApiService())
    private val mHistoryItemRepository = HistoryItemRepository(application)

    fun postLabel(label: String): LiveData<Resource<List<LabelPostResponse>>> {
        val result = MutableLiveData<Resource<List<LabelPostResponse>>>()

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiRepository.postLabel(LabelRequest(label))
                }
                result.postValue(response)
            } catch (e: Exception) {
                result.postValue(Resource.Error(e.localizedMessage ?: "An error occurred"))
            }
        }

        return result
    }

    fun saveHistory(historyItem: HistoryItem){
        mHistoryItemRepository.insert(historyItem)
    }
}