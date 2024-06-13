package com.grassterra.fitassist.response

import com.google.gson.annotations.SerializedName

data class WorkoutVideoResponse(

	@field:SerializedName("list_video")
	val listVideo: List<ListVideoItem> = emptyList(),

	@field:SerializedName("error")
	val error: Boolean? = true,

	@field:SerializedName("message")
	val message: String? = null
)

data class ListVideoItem(

	@field:SerializedName("name_exercise")
	val nameExercise: String? = null,

	@field:SerializedName("url_video")
	val urlVideo: String? = null,

	@field:SerializedName("name_equipment")
	val nameEquipment: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("label")
	val label: String? = null
)
