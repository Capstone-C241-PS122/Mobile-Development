package com.grassterra.fitassist.repository

import com.grassterra.fitassist.helper.Resource
import com.grassterra.fitassist.response.FeedbackResponse
import com.grassterra.fitassist.response.LibraryGetResponse
import com.grassterra.fitassist.response.NutritionPostResponse
import com.grassterra.fitassist.response.WorkoutArticleResponse
import com.grassterra.fitassist.response.WorkoutVideoResponse
import com.grassterra.fitassist.retrofit.ApiService
import kotlinx.coroutines.processNextEventInCurrentThread

class ApiRepository(private val apiService: ApiService) {

    suspend fun postNutrition(name: String, weight: Int): Resource<NutritionPostResponse>{
        return try {
            val resp = apiService.postNutrition(name, weight)
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

    suspend fun getArticle(id: Int): Resource<WorkoutArticleResponse>{
        return try {
            val resp = apiService.getArticle(id = id)
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

    suspend fun getAllVideo(): Resource<WorkoutVideoResponse>{
        return try {
            val resp = apiService.getAllVideo()
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

    suspend fun postFeedback(): Resource<FeedbackResponse>{
        return try {
            val resp = apiService.postFeedback()
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
}