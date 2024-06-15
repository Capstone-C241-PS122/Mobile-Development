package com.grassterra.fitassist.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grassterra.fitassist.database.history.HistoryItem
import com.grassterra.fitassist.database.user.Userdata
import com.grassterra.fitassist.helper.Resource
import com.grassterra.fitassist.repository.ApiRepository
import com.grassterra.fitassist.repository.FavoriteExerciseRepository
import com.grassterra.fitassist.repository.UserRepository
import com.grassterra.fitassist.response.ListArticleItem
import com.grassterra.fitassist.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel(application: Application): ViewModel() {

    private val _articleList = MutableLiveData<List<ListArticleItem>>()
    val articleList: LiveData<List<ListArticleItem>> = _articleList

    private val mUserRepository: UserRepository = UserRepository(application)
    private val mFavoriteExerciseRepository: FavoriteExerciseRepository = FavoriteExerciseRepository(application)
    private val apiRepository: ApiRepository = ApiRepository(ApiConfig.getApiService())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val bodyparts = mFavoriteExerciseRepository.getALl()
            if(bodyparts.size == 0){
                getArticle()
            }else{
                val randomBodyPart = bodyparts.random()
                getArticleBodyPart(randomBodyPart.name)
            }
        }
    }

    fun getArticleBodyPart(bodypart: String){
        viewModelScope.launch {
            val res = apiRepository.getArticles(bodypart)
            when (res){
                is Resource.Success ->{
                    _articleList.value = res.data.listArticle
                }
                is Resource.Error ->{
                    Log.d("MainViewModel", res.errorMessage)
                }
            }
        }
    }

    fun getArticle(){
        viewModelScope.launch {
            val res = apiRepository.getArticles2()
            when (res){
                is Resource.Success ->{
                    _articleList.value = res.data.listArticle
                }
                is Resource.Error ->{
                    Log.d("MainViewModel", res.errorMessage)
                }
            }
        }
    }

    fun insertUser(userdata: Userdata){
        mUserRepository.insert(userdata)
    }

    fun overwriteUser(userdata: Userdata){
        mUserRepository.deleteAllUser()
        mUserRepository.insert(userdata)
    }

    fun isEmpty(): Boolean{
        val count = mUserRepository.getUserCount()
        return count == 0
    }

    fun getUser(): Userdata?{
        return mUserRepository.getCurrentUser()
    }
}