package com.grassterra.fitassist.repository

import android.app.Application
import com.grassterra.fitassist.database.history.HistoryItem
import com.grassterra.fitassist.database.history.HistoryItemDao
import com.grassterra.fitassist.database.history.HistoryItemRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HistoryItemRepository(application: Application) {
    private val mHistoryItemDao: HistoryItemDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val dbHistoryItem = HistoryItemRoomDatabase.getDatabase(application)
        mHistoryItemDao = dbHistoryItem.historyItemDao()
    }

    fun insert(historyItem: HistoryItem){
        executorService.execute{mHistoryItemDao.insert(historyItem)}
    }

    fun clear(){
        executorService.execute{mHistoryItemDao.deleteALlHistoryItem()}
    }

    fun getAll(): MutableList<HistoryItem>{
        return mHistoryItemDao.getAllHistoryItem()
    }
}