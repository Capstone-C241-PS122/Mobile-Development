package com.grassterra.fitassist.repository

import android.app.Application
import com.grassterra.fitassist.database.exercise.FavoriteExercise
import com.grassterra.fitassist.database.exercise.FavoriteExerciseDao
import com.grassterra.fitassist.database.exercise.FavoriteExerciseRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteExerciseRepository(application: Application) {
    private val mExerciseDao: FavoriteExerciseDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val dbExercise = FavoriteExerciseRoomDatabase.getDatabase(application)
        mExerciseDao = dbExercise.favoriteExerciseDao()
    }

    fun insert(exercise: FavoriteExercise){
        executorService.execute{mExerciseDao.insert(exercise)}
    }

    fun delete(exercise: FavoriteExercise){
        executorService.execute{mExerciseDao.delete(exercise)}
    }

    fun clear(){
        executorService.execute{mExerciseDao.deleteAllFavoriteExercise()}
    }

    fun getALl(): MutableList<FavoriteExercise>{
        return mExerciseDao.getAllExercise()
    }
}