package com.grassterra.fitassist.database.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryItemDao {
    @Insert
    fun insert(historyItem: HistoryItem)

    @Query("SELECT * from HistoryItem")
    fun getAllHistoryItem(): MutableList<HistoryItem>

    @Query("DELETE FROM HistoryItem")
    fun deleteALlHistoryItem()
}