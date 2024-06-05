package com.grassterra.fitassist.helper

import android.os.Parcel
import android.os.Parcelable

data class ParcelableMap(val map: Map<String, Float>) : Parcelable {
    constructor(parcel: Parcel) : this(
        mutableMapOf<String, Float>().apply {
            val size = parcel.readInt()
            repeat(size) {
                val key = parcel.readString()!!
                val value = parcel.readFloat()
                put(key, value)
            }
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(map.size)
        map.forEach { (key, value) ->
            parcel.writeString(key)
            parcel.writeFloat(value)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableMap> {
        override fun createFromParcel(parcel: Parcel): ParcelableMap {
            return ParcelableMap(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableMap?> {
            return arrayOfNulls(size)
        }
    }
}