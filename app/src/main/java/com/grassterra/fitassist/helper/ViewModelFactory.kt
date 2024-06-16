package com.grassterra.fitassist.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grassterra.fitassist.ui.article.DetailsArticleViewModel
import com.grassterra.fitassist.ui.myBody.BMIViewModel
import com.grassterra.fitassist.ui.exercise.DetailExerciseViewModel
import com.grassterra.fitassist.ui.history.HistoryViewModel
import com.grassterra.fitassist.ui.mainMenu.HomeViewModel
import com.grassterra.fitassist.ui.library.LibraryVideoViewModel
import com.grassterra.fitassist.ui.mainMenu.MainViewModel
import com.grassterra.fitassist.ui.userInput.SelectExerciseViewModel

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
        else if (modelClass.isAssignableFrom(DetailExerciseViewModel::class.java)) {
            return DetailExerciseViewModel(mApplication) as T
        }
        else if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(mApplication) as T
        }
        else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(mApplication) as T
        }
        else if (modelClass.isAssignableFrom(DetailsArticleViewModel::class.java)) {
            return DetailsArticleViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}