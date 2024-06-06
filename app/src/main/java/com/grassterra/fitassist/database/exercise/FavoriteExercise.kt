package com.grassterra.fitassist.database.exercise

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteExercise(
    @PrimaryKey(autoGenerate = false)

    @ColumnInfo(name = "name")
    var name: String
): Parcelable