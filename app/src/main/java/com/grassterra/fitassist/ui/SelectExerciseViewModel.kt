package com.grassterra.fitassist.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import com.grassterra.fitassist.database.exercise.FavoriteExercise
import com.grassterra.fitassist.repository.FavoriteExerciseRepository
import com.grassterra.fitassist.repository.UserRepository

class SelectExerciseViewModel(application: Application): ViewModel() {

    private val mFavoriteExerciseRepository: FavoriteExerciseRepository = FavoriteExerciseRepository(application)

    fun insert(exercise: String){
        mFavoriteExerciseRepository.insert(FavoriteExercise(exercise))
    }

    fun clear(){
        mFavoriteExerciseRepository.clear()
    }

    fun delete(exercise: String){
        val exerciseList: MutableList<FavoriteExercise> = mFavoriteExerciseRepository.getALl()
        val deleteExercise = exerciseList.find { it.name == exercise }
        if (deleteExercise != null) {
            mFavoriteExerciseRepository.delete(deleteExercise)
        }
    }

    fun toggle(exercise: String){
        val exerciseList: MutableList<FavoriteExercise> = mFavoriteExerciseRepository.getALl()
        val deleteExercise = exerciseList.find { it.name == exercise }
        if (deleteExercise != null) {
            mFavoriteExerciseRepository.delete(deleteExercise)
        }
        else{
            mFavoriteExerciseRepository.insert(FavoriteExercise(exercise))
        }
    }

    fun getAll(): List<FavoriteExercise>{
        return mFavoriteExerciseRepository.getALl()
    }
}