package com.grassterra.fitassist.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Userdata(
    @PrimaryKey(autoGenerate = false)

    @ColumnInfo(name = "weight")
    var weight: Int? = null,

    @ColumnInfo(name = "height")
    var height: Int? = null,

    @ColumnInfo(name = "age")
    var age: Int? = null
): Parcelable