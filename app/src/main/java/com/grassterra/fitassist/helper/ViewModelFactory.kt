package com.grassterra.fitassist.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grassterra.fitassist.ui.BMIViewModel
import com.grassterra.fitassist.ui.LibraryVideoViewModel
import com.grassterra.fitassist.ui.MainViewModel
import com.grassterra.fitassist.ui.SelectExerciseViewModel

class ViewModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(mApplication) as T
        }
        else if (modelClass.isAssignableFrom(SelectExerciseViewModel::class.java)) {
            return SelectExerciseViewModel(mApplication) as T
        }
        else if (modelClass.isAssignableFrom(BMIViewModel::class.java)) {
            return BMIViewModel(mApplication) as T
        }
        else if (modelClass.isAssignableFrom(LibraryVideoViewModel::class.java)) {
            return LibraryVideoViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}