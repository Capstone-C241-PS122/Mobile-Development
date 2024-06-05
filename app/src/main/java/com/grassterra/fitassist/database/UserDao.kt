package com.grassterra.fitassist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insert(user: Userdata)

    @Delete
    fun delete(user: Userdata)

    @Query("SELECT * from Userdata")
    fun getAllFavoriteUser(): LiveData<List<Userdata>>
}