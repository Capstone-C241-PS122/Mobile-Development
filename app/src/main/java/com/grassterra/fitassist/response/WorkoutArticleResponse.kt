package com.grassterra.fitassist.response

import com.google.gson.annotations.SerializedName

data class WorkoutArticleResponse(

	@field:SerializedName("Desc")
	val desc: String? = null,

	@field:SerializedName("BodyPart")
	val bodyPart: String? = null,

	@field:SerializedName("Type")
	val type: String? = null,

	@field:SerializedName("Equipment")
	val equipment: String? = null,

	@field:SerializedName("Title")
	val title: String? = null,

	@field:SerializedName("Level")
	val level: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("error")
	val error: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
