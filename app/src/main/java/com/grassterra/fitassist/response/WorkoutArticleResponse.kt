package com.grassterra.fitassist.response

import com.google.gson.annotations.SerializedName

data class WorkoutArticleResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("article")
	val article: Article
)

data class Article(

	@field:SerializedName("BodyPart")
	val bodyPart: String,

	@field:SerializedName("Type")
	val type: String,

	@field:SerializedName("Description")
	val description: String,

	@field:SerializedName("Equipment")
	val equipment: String,

	@field:SerializedName("Title")
	val title: String,

	@field:SerializedName("Level")
	val level: String,

	@field:SerializedName("id")
	val id: Int
)
