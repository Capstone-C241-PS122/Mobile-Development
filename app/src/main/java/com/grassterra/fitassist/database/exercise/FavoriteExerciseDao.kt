package com.grassterra.fitassist.database.exercise

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteExerciseDao {
    @Insert
    fun insert(favoriteExercise: FavoriteExercise)

    @Delete
    fun delete(favoriteExercise: FavoriteExercise)

    @Query("DELETE FROM FavoriteExercise")
    fun deleteAllFavoriteExercise()

    @Query("SELECT * from FavoriteExercise")
    fun getAllExercise(): MutableList<FavoriteExercise>
}