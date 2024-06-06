package com.grassterra.fitassist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.grassterra.fitassist.R
import com.grassterra.fitassist.databinding.ActivityBmiBinding

class ActivityBMI : AppCompatActivity() {
    private lateinit var binding:ActivityBmiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCalculate.setOnClickListener {
            calculateBMI()
        }
        binding.btnReset.setOnClickListener{
            resetFields()
        }
    }
    private fun resetFields() {
        binding.etHeight.text.clear()
        binding.etWeight.text.clear()
        binding.tvResult.text = ""
        binding.imgResult.setImageResource(0)
    }
    private fun calculateBMI() {
        val heightStr = binding.etHeight.text.toString()
        val weightStr = binding.etWeight.text.toString()

        if (heightStr.isNotEmpty() && weightStr.isNotEmpty()) {
            val height = heightStr.toFloat() / 100
            val weight = weightStr.toFloat()
            val bmi = weight / (height * height)

            val bmiCategory: String
            val imgResource: Int

            when {
                bmi < 18.5 -> {
                    bmiCategory = "Underweight"
                    imgResource = R.drawable.underweight
                }

                bmi < 24.9 -> {
                    bmiCategory = "Normal weight"
                    imgResource = R.drawable.normal_weight
                }

                bmi < 29.9 -> {
                    bmiCategory = "Overweight"
                    imgResource = R.drawable.overweight
                }

                else -> {
                    bmiCategory = "Obesity"
                    imgResource = R.drawable.obesity
                }
            }

            binding.tvResult.text = String.format("BMI: %.2f (%s)", bmi, bmiCategory)
            binding.imgResult.setImageResource(imgResource)
        }
    }
}