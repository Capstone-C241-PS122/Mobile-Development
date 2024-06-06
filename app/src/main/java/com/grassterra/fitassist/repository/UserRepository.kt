package com.grassterra.fitassist.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.grassterra.fitassist.database.user.UserDao
import com.grassterra.fitassist.database.user.UserRoomDatabase
import com.grassterra.fitassist.database.user.Userdata
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val mUserDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserRoomDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }

    fun getAllUser(): LiveData<List<Userdata>> = mUserDao.getAllUser()

    fun insert(user: Userdata){
        executorService.execute{mUserDao.insert(user)}
    }

    fun delete(user: Userdata){
        executorService.execute{mUserDao.delete(user)}
    }

    fun getUserCount() : Int{
        return mUserDao.getUserCount()
    }

    fun deleteAllUser(){
        executorService.execute{mUserDao.deleteAllUsers()}
    }

    fun getCurrentUser(): Userdata?{
        return mUserDao.getFirstUser()
    }

}