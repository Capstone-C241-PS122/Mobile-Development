package com.grassterra.fitassist.helper

import com.google.gson.annotations.SerializedName

data class LabelRequest(
    @SerializedName("label") val label: String
)