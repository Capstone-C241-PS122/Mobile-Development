package com.grassterra.fitassist.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grassterra.fitassist.databinding.ActivityCalculateNutritionBinding
import com.grassterra.fitassist.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ActivityCalculateNutrition : AppCompatActivity() {
    private lateinit var binding: ActivityCalculateNutritionBinding
    private val apiService = ApiConfig.getApiService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculateNutritionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCalculate.setOnClickListener {
            resultCalculate()
        }
    }
    private fun resultCalculate() {
        val foodName = binding.editTextFood.text.toString().trim()
        val foodWeight = binding.editTextWeight.text.toString().toIntOrNull() ?: 0
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = apiService.postNutrition(name = foodName, weight = foodWeight)
                if (response != null && response.message == "Prediksi Nutrisi Berhasil") {
                    val resultText = StringBuilder().apply {
                        append("Calories: ${response.calories ?: "N/A"}\n")
                        append("Protein: ${response.proteins ?: "N/A"}\n")
                        append("Carbs: ${response.carbohydrate ?: "N/A"}\n")
                        append("Fat: ${response.fat ?: "N/A"}")
                    }.toString()

                    binding.textViewResult.text = resultText
                    showToast("Data retrieved successfully")
                } else {
                    showToast("Error: Invalid response or message")
                }
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> showToast("Error: ${e.message()}")
                    else -> showToast("Error: ${e.message}")
                }
            } finally {
                binding.editTextFood.text.clear()
                binding.editTextWeight.text.clear()
            }
        }

    }
    private fun showToast(message: String) {
        Toast.makeText(this@ActivityCalculateNutrition, message, Toast.LENGTH_SHORT).show()
    }
}
