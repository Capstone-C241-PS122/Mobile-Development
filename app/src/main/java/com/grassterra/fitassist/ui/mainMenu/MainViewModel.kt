package com.grassterra.fitassist.ui.mainMenu

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

    private val mUserRepository: UserRepository = UserRepository(application)

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