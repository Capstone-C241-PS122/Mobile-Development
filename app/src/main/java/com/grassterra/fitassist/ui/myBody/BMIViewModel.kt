package com.grassterra.fitassist.ui.myBody

import android.app.Application
import androidx.lifecycle.ViewModel
import com.grassterra.fitassist.database.user.Userdata
import com.grassterra.fitassist.repository.UserRepository

class BMIViewModel(application: Application): ViewModel() {

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