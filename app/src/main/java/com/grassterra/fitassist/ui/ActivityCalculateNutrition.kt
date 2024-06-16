package com.grassterra.fitassist.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grassterra.fitassist.databinding.ActivityCalculateNutritionBinding
import com.grassterra.fitassist.helper.Resource
import com.grassterra.fitassist.repository.ApiRepository
import com.grassterra.fitassist.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ActivityCalculateNutrition : AppCompatActivity() {

    private lateinit var binding: ActivityCalculateNutritionBinding
    private lateinit var apiRepository: ApiRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculateNutritionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiRepository = ApiRepository(apiService)
        binding.buttonCalculate.setOnClickListener {
            resultCalculate()
        }
    }

    private val apiService = ApiConfig.getApiService()
    private fun resultCalculate() {
        val foodName = binding.editTextFood.text.toString().trim()
        val foodWeight = binding.editTextWeight.text.toString().toIntOrNull() ?: 0

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = apiRepository.postNutrition(foodName, foodWeight)
                when (result) {
                    is Resource.Success -> {
                        val response = result.data
                        if (response?.message == "Prediksi Nutrisi Berhasil") {
                            val predictedNutrition = response.predictedNutrition
                            val calories = predictedNutrition?.calories ?: 0
                            val proteins = predictedNutrition?.proteins ?: 0.0
                            val carbohydrates = predictedNutrition?.carbohydrate ?: 0
                            val fat = predictedNutrition?.fat ?: 0.0
                            val caloriesText = "Calories: $calories\n"
                            val proteinsText = "Proteins: $proteins\n"
                            val carbohydratesText = "Carbohydrates: $carbohydrates\n"
                            val fatText = "Fat: $fat"
                            val resultText = "$caloriesText$proteinsText$carbohydratesText$fatText"

                            withContext(Dispatchers.Main) {
                                binding.textViewCalories.text = caloriesText
                                binding.textViewProteins.text = proteinsText
                                binding.textViewCarbohydrates.text = carbohydratesText
                                binding.textViewFat.text = fatText

                                showToast("Data retrieved successfully")
                            }
                        } else {
                            showToast("Error: Invalid response or message")
                        }
                    }
                    is Resource.Error -> {
                        showToast("Error: ${result.errorMessage ?: "Unknown error"}")
                    }
                }
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> showToast("Error: ${e.message()}")
                    else -> showToast("Error: ${e.message}")
                }
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this@ActivityCalculateNutrition, message, Toast.LENGTH_SHORT).show()
    }

}
