package com.grassterra.fitassist.retrofit

import com.grassterra.fitassist.response.FeedbackResponse
import com.grassterra.fitassist.response.LibraryGetResponse
import com.grassterra.fitassist.response.NutritionPostResponse
import com.grassterra.fitassist.response.SearchResponse
import com.grassterra.fitassist.response.WorkoutArticleResponse
import com.grassterra.fitassist.response.WorkoutVideoResponse
import retrofit2.Call
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

    @FormUrlEncoded
    @POST("body/nutrition")
    suspend fun postNutrition(
        @Field("name") name: String? = null,
        @Field("weight") weight: Int? = 0
    ): NutritionPostResponse

    @GET("library")
    suspend fun getLibrary(): LibraryGetResponse

    @GET("article/{id}")
    suspend fun getArticle(
        @Path("id") id: Int? = 1
    ): WorkoutArticleResponse

    @GET("workout/video")
    suspend fun getAllVideo(): WorkoutVideoResponse

    @POST("feedback")
    suspend fun postFeedback(
        @Field("description") description: String?=null
    ): FeedbackResponse
}