package com.grassterra.fitassist.retrofit

import com.google.gson.annotations.SerializedName
import com.grassterra.fitassist.helper.LabelRequest
import com.grassterra.fitassist.response.FeedbackResponse
import com.grassterra.fitassist.response.LabelPostResponse
import com.grassterra.fitassist.response.LibraryGetResponse
import com.grassterra.fitassist.response.NutritionPostResponse
import com.grassterra.fitassist.response.SearchResponse
import com.grassterra.fitassist.response.WorkoutArticleResponse
import com.grassterra.fitassist.response.WorkoutVideoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/article")
    suspend fun searchArticle(
        @Query("Title") Title: String
    ): Call<SearchResponse>

    @GET("nutrition")
    suspend fun postNutrition(
        @Query("name") name: String? = null,
        @Query("weight") weight: Int? = 0
    ): NutritionPostResponse

    @GET("library")
    suspend fun getLibrary(): LibraryGetResponse

    @GET("article/{id}")
    suspend fun getArticle(
        @Path("id") id: Int? = 1
    ): WorkoutArticleResponse

    @GET("video")
    suspend fun getAllVideo(): WorkoutVideoResponse

    @POST("feedback")
    suspend fun postFeedback(
        @Field("description") description: String?=null
    ): FeedbackResponse

    @POST("up/video")
    suspend fun postLabel(
        @Body request: LabelRequest
    ): List<LabelPostResponse>
}