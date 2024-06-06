package com.grassterra.fitassist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.grassterra.fitassist.databinding.ActivityBmrBinding

class ActivityBMR : AppCompatActivity() {
    private lateinit var binding: ActivityBmrBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnCalculate.setOnClickListener{
            calculateBMR()
        }
    }
    private fun calculateBMR() {
        val weight = binding.etWeight.text.toString().toDoubleOrNull() ?: 0.0
        val height = binding.etHeight.text.toString().toDoubleOrNull() ?: 0.0
        val age = binding.etAge.text.toString().toIntOrNull() ?: 0
        val gender = binding.etGender.text.toString()

        val bmr = if (gender.equals("pria", ignoreCase = true)) {
            88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)
        } else {
            447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)
        }
        val roundBmr = bmr.toInt()

        binding.resultTextView.text = "Hasil BMR: $roundBmr kalori per hari"
    }
}