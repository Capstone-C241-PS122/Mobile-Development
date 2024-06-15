package com.grassterra.fitassist.repository

import com.grassterra.fitassist.helper.LabelRequest
import com.grassterra.fitassist.helper.Resource
import com.grassterra.fitassist.response.ArticlesResponse
import com.grassterra.fitassist.response.FeedbackResponse
import com.grassterra.fitassist.response.LabelPostResponse
import com.grassterra.fitassist.response.LibraryGetResponse
import com.grassterra.fitassist.response.ListVideoItem
import com.grassterra.fitassist.response.NutritionResponse
import com.grassterra.fitassist.response.WorkoutArticleResponse
import com.grassterra.fitassist.retrofit.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ApiRepository(private val apiService: ApiService) {

    suspend fun getArticles(bodypart:String): Resource<ArticlesResponse>{
        return try {
            val resp = apiService.getArticle(bodypart)
            if (resp.error == false) {
                Resource.Success(resp)
            } else {
                Resource.Error(resp.message)
            }
        } catch (e: Exception){
            Resource.Error(e.message.toString())
        }
    }

    suspend fun getArticles2(): Resource<ArticlesResponse>{
        return try {
            val resp = apiService.getAllArticle()
            if (resp.error == false) {
                Resource.Success(resp)
            } else {
                Resource.Error(resp.message)
            }
        } catch (e: Exception){
            Resource.Error(e.message.toString())
        }
    }

    suspend fun postNutrition(name: String, weight: Int): Resource<NutritionResponse> {
        return try {
            val resp = apiService.postNutrition(name, weight)
            Resource.Success(resp)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Exception occurred")
        }
    }

    suspend fun getLibrary(): Resource<LibraryGetResponse>{
        return try {
            val resp = apiService.getLibrary()
            if (resp.error == false){
                Resource.Success(resp)
            }
            else{
                Resource.Error(resp.message.toString())
            }
        } catch (e: Exception){
            Resource.Error(e.message.toString())
        }
    }

    suspend fun getDetailArticle(id: Int): Resource<WorkoutArticleResponse>{
        return try {
            val resp = apiService.getDetailArticle(id = id)
            if (resp.error == "false"){
                Resource.Success(resp)
            }
            else{
                Resource.Error(resp.message.toString())
            }
        } catch (e:Exception){
            Resource.Error(e.message.toString())
        }
    }

    suspend fun getAllVideo(): Resource<List<ListVideoItem>>{
        val resp = apiService.getAllVideo()
        return Resource.Success(resp)
    }

    suspend fun postFeedback(description: String): Resource<FeedbackResponse> {
        return try {
            val jsonObject = JSONObject().apply {
                put("description", description)
            }
            val requestBody = jsonObject.toString()
                .toRequestBody("application/json".toMediaTypeOrNull())

            val response = apiService.postFeedback(requestBody)
            if (!response.error!!) {
                Resource.Success(response)
            } else {
                Resource.Error(response.message ?: "Unknown error")
            }
        } catch (e: Exception) {
            Resource.Error("Error: ${e.message}")
        }
    }

    suspend fun postLabel(labelRequest: LabelRequest): Resource<List<LabelPostResponse>>{
        val resp = apiService.postLabel(labelRequest)
        return Resource.Success(resp)
    }

}