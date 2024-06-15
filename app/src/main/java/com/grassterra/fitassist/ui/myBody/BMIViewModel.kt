package com.grassterra.fitassist.ui.myBody

import android.app.Application
import androidx.lifecycle.ViewModel
import com.grassterra.fitassist.database.user.Userdata
import com.grassterra.fitassist.repository.UserRepository

class BMIViewModel(application: Application): ViewModel() {

    private val mUserRepository: UserRepository = UserRepository(application)

    fun getUser(): Userdata?{
        return mUserRepository.getCurrentUser()
    }
}