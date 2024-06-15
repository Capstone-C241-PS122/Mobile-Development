package com.grassterra.fitassist.response

import com.google.gson.annotations.SerializedName

data class ArticlesResponse(

	@field:SerializedName("list_article")
	val listArticle: List<ListArticleItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class ListArticleItem(

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
