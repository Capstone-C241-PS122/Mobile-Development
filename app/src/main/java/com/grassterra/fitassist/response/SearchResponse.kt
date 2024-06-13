package com.grassterra.fitassist.response

import com.google.gson.annotations.SerializedName

data class SearchResponse(

	@field:SerializedName("list_article")
	val listArticle: List<ListArticleItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ListArticleItem(

	@field:SerializedName("Title")
	val title: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
