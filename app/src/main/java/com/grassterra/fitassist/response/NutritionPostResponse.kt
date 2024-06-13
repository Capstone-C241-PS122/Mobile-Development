package com.grassterra.fitassist.response

import com.google.gson.annotations.SerializedName

data class NutritionPostResponse(

	@field:SerializedName("proteins")
	val proteins: Any? = null,

	@field:SerializedName("fat")
	val fat: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("calories")
	val calories: Int? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("carbohydrate")
	val carbohydrate: Int? = null
)
