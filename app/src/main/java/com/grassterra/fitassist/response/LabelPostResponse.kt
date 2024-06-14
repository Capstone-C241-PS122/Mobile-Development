package com.grassterra.fitassist.response

import com.google.gson.annotations.SerializedName

data class LabelPostResponse(

	@field:SerializedName("name_exercise")
	val nameExercise: String,

	@field:SerializedName("url_video")
	val urlVideo: String,

	@field:SerializedName("name_equipment")
	val nameEquipment: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("label")
	val label: String,

	@field:SerializedName("bodypart")
	val bodypart: String
)
