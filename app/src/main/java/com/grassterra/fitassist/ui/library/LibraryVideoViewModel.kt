package com.grassterra.fitassist.ui.library

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grassterra.fitassist.helper.Resource
import com.grassterra.fitassist.repository.ApiRepository
import com.grassterra.fitassist.response.ListVideoItem
import com.grassterra.fitassist.retrofit.ApiConfig
import kotlinx.coroutines.launch

class LibraryVideoViewModel(application: Application): ViewModel() {
    private val _videoList = MutableLiveData<List<ListVideoItem>?>()
    val videoList: MutableLiveData<List<ListVideoItem>?> = _videoList

    init {
        val apiRepository = ApiRepository(ApiConfig.getApiService())
        getAllVideo(apiRepository)
    }

    fun getAllVideo(apiRepository: ApiRepository){
        viewModelScope.launch {
            val res = apiRepository.getAllVideo()
            when (res){
                is Resource.Success ->{
                    _videoList.value = res.data
                }
                is Resource.Error ->{
                    Log.d("LibraryViewModel", res.errorMessage)
                }
            }
        }
    }
}