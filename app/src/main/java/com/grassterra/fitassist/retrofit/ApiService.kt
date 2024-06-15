package com.grassterra.fitassist.retrofit

import com.grassterra.fitassist.helper.LabelRequest
import com.grassterra.fitassist.response.ArticlesResponse
import com.grassterra.fitassist.response.FeedbackResponse
import com.grassterra.fitassist.response.LabelPostResponse
import com.grassterra.fitassist.response.LibraryGetResponse
import com.grassterra.fitassist.response.ListVideoItem
import com.grassterra.fitassist.response.NutritionResponse
import com.grassterra.fitassist.response.WorkoutArticleResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("articles")
    suspend fun getArticle(
        @Query("bodypart") bodypart: String? = null
    ): ArticlesResponse

    @GET("articles")
    suspend fun getAllArticle(
    ): ArticlesResponse

    @GET("nutrition")
    suspend fun postNutrition(
        @Query("name") name: String? = null,
        @Query("weight") weight: Int? = 0
    ): NutritionResponse

    @GET("library")
    suspend fun getLibrary(): LibraryGetResponse

    @GET("articles/{id}")
    suspend fun getDetailArticle(
        @Path("id") id: Int? = 1
    ): WorkoutArticleResponse

    @GET("video")
    suspend fun getAllVideo(): List<ListVideoItem>

    @POST("feedback")
    suspend fun postFeedback(@Body requestBody: RequestBody): FeedbackResponse

    @POST("up/video")
    suspend fun postLabel(
        @Body request: LabelRequest
    ): List<LabelPostResponse>
}