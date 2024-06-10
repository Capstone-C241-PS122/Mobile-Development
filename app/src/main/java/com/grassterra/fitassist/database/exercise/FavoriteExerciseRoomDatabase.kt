package com.grassterra.fitassist.database.exercise

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.grassterra.fitassist.database.user.UserDao
import com.grassterra.fitassist.database.user.Userdata

@Database(entities = [FavoriteExercise::class], version = 1)
abstract class FavoriteExerciseRoomDatabase: RoomDatabase() {
    abstract fun favoriteExerciseDao(): FavoriteExerciseDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteExerciseRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavoriteExerciseRoomDatabase {
            if (INSTANCE == null) {
                synchronized(FavoriteExerciseRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavoriteExerciseRoomDatabase::class.java, "exercise_database")
                        .build()
                }
            }
            return INSTANCE as FavoriteExerciseRoomDatabase
        }
    }
}