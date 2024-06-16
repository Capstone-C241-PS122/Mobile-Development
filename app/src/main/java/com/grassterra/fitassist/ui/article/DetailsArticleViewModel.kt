package com.grassterra.fitassist.ui.article

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grassterra.fitassist.helper.LabelRequest
import com.grassterra.fitassist.helper.Resource
import com.grassterra.fitassist.repository.ApiRepository
import com.grassterra.fitassist.response.LabelPostResponse
import com.grassterra.fitassist.response.WorkoutArticleResponse
import com.grassterra.fitassist.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsArticleViewModel(application: Application): ViewModel() {
    private val apiRepository = ApiRepository(ApiConfig.getApiService())

    fun setArticle(id: Int): LiveData<Resource<WorkoutArticleResponse>>{
        val result = MutableLiveData<Resource<WorkoutArticleResponse>>()

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiRepository.getDetailArticle(id)
                }
                result.postValue(response)
            } catch (e: Exception) {
                result.postValue(Resource.Error(e.localizedMessage ?: "An error occurred"))
            }
        }
        return result
    }
}