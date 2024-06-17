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
        reGetArticles()
    }

    fun reGetArticles(){
        viewModelScope.launch(Dispatchers.IO) {
            val bodyparts = mFavoriteExerciseRepository.getALl()
            if(bodyparts.size == 0){
                Log.d("not in",bodyparts.toString())
                getArticle()
            }else{
                val randomBodyPart = bodyparts.random()
                Log.d("in",randomBodyPart.name)
                getArticleBodyPart(randomBodyPart.name)
            }
        }
    }

    private fun getArticleBodyPart(bodypart: String){
        viewModelScope.launch {
            _isLoading.value = true
            val res = apiRepository.getArticles(bodypart)
            when (res){
                is Resource.Success ->{
                    _articleList.value = res.data.listArticle.take(20).shuffled()
                }
                is Resource.Error ->{
                    Log.d("MainViewModel", res.errorMessage)
                }
            }
            _isLoading.value = false
        }
    }

    private fun getArticle(){
        viewModelScope.launch {
            _isLoading.value = true
            val res = apiRepository.getArticles2()
            when (res){
                is Resource.Success ->{
                    _articleList.value = res.data.listArticle.take(20).shuffled()
                }
                is Resource.Error ->{
                    Log.d("MainViewModel", res.errorMessage)
                }
            }
            _isLoading.value = false
        }
    }
}