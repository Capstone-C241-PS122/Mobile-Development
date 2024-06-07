package com.grassterra.fitassist.ui

import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.grassterra.fitassist.databinding.ActivityBmrBinding
import com.grassterra.fitassist.helper.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityBMR : AppCompatActivity() {
    private lateinit var binding: ActivityBmrBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bmiViewModel = obtainViewModel(this@ActivityBMR)
        setFields(bmiViewModel)

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

    private fun setFields(bmiViewModel: BMIViewModel) {
        lifecycleScope.launch(Dispatchers.IO) {
            val height = bmiViewModel.getUser()?.height.toString()
            val weight = bmiViewModel.getUser()?.weight.toString()
            val age = bmiViewModel.getUser()?.age.toString()
            val gender = if (bmiViewModel.getUser()?.gender == true) "pria" else "wanita"

            withContext(Dispatchers.Main) {
                binding.etHeight.text = Editable.Factory.getInstance().newEditable(height)
                binding.etWeight.text = Editable.Factory.getInstance().newEditable(weight)
                binding.etAge.text = Editable.Factory.getInstance().newEditable(age)
                binding.etGender.text = Editable.Factory.getInstance().newEditable(gender)
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): BMIViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(BMIViewModel::class.java)
    }
}