package com.grassterra.fitassist.database.history

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class HistoryItem (
    @PrimaryKey(autoGenerate = false)

    @ColumnInfo(name = "equipment")
    var equipment: String? = "",

    @ColumnInfo(name = "bodypart")
    var bodypart: String? = "",

    @ColumnInfo(name = "imageuri")
    var imageuri: String? = null,

    @ColumnInfo(name = "exercise")
    var exercise: String? = null,

    @ColumnInfo(name = "date")
    var date: String? = null
): Parcelable