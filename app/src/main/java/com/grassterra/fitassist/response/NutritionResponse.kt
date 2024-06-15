package com.grassterra.fitassist.response

import com.google.gson.annotations.SerializedName

data class NutritionResponse(

	@field:SerializedName("predictedNutrition")
	val predictedNutrition: PredictedNutrition? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
)

data class PredictedNutrition(

	@field:SerializedName("proteins")
	val proteins: Any? = null,

	@field:SerializedName("fat")
	val fat: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("calories")
	val calories: Int? = null,

	@field:SerializedName("carbohydrate")
	val carbohydrate: Int? = null
)
