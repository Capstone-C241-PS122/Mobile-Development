package com.grassterra.fitassist.ui.mainMenu

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grassterra.fitassist.helper.Resource
import com.grassterra.fitassist.repository.ApiRepository
import com.grassterra.fitassist.repository.FavoriteExerciseRepository
import com.grassterra.fitassist.response.ListArticleItem
import com.grassterra.fitassist.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application): ViewModel() {
    private val _articleList = MutableLiveData<List<ListArticleItem>>()
    val articleList: LiveData<List<ListArticleItem>> = _articleList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val apiRepository: ApiRepository = ApiRepository(ApiConfig.getApiService())
    private val mFavoriteExerciseRepository: FavoriteExerciseRepository = FavoriteExerciseRepository(application)

    init {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val bodyparts = mFavoriteExerciseRepository.getALl()
            if(bodyparts.size == 0){
                Log.d("not in",bodyparts.toString())
                getArticle()
            }else{
                Log.d("in",bodyparts.toString())
                val randomBodyPart = bodyparts.random()
                Log.d("in",randomBodyPart.name)
                getArticleBodyPart(randomBodyPart.name)
            }
            withContext(Dispatchers.Main) {
                _isLoading.value = false
            }
        }
    }

    private fun getArticleBodyPart(bodypart: String){
        viewModelScope.launch {
            val res = apiRepository.getArticles(bodypart)
            when (res){
                is Resource.Success ->{
                    _articleList.value = res.data.listArticle.take(20)
                }
                is Resource.Error ->{
                    Log.d("MainViewModel", res.errorMessage)
                }
            }
        }
    }

    private fun getArticle(){
        viewModelScope.launch {
            val res = apiRepository.getArticles2()
            when (res){
                is Resource.Success ->{
                    _articleList.value = res.data.listArticle.take(20)
                }
                is Resource.Error ->{
                    Log.d("MainViewModel", res.errorMessage)
                }
            }
        }
    }
}