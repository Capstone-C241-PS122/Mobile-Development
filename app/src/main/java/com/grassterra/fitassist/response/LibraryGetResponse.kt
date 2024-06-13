package com.grassterra.fitassist.response

import com.google.gson.annotations.SerializedName

data class LibraryGetResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("list_library")
	val listLibrary: List<ListLibraryItem?>? = null
)

data class ListLibraryItem(

	@field:SerializedName("id_library")
	val idLibrary: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)
