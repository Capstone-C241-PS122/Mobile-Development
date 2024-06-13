package com.grassterra.fitassist.database.history

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryItem::class], version = 1)
abstract class HistoryItemRoomDatabase : RoomDatabase() {
    abstract fun historyItemDao(): HistoryItemDao
    companion object {
        @Volatile
        private var INSTANCE: HistoryItemRoomDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): HistoryItemRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryItemRoomDatabase::class.java,
                    "history_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}