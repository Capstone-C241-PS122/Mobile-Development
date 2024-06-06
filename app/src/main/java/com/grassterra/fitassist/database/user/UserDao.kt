package com.grassterra.fitassist.database.user

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
    fun getAllUser(): LiveData<List<Userdata>>

    @Query("SELECT COUNT(*) from Userdata")
    fun getUserCount(): Int

    @Query("DELETE FROM Userdata")
    fun deleteAllUsers()

    @Query("SELECT * FROM Userdata LIMIT 1")
    fun getFirstUser(): Userdata?
}