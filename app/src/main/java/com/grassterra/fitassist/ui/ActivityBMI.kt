package com.grassterra.fitassist.ui

import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.grassterra.fitassist.R
import com.grassterra.fitassist.databinding.ActivityBmiBinding
import com.grassterra.fitassist.helper.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityBMI : AppCompatActivity() {
    private lateinit var binding:ActivityBmiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bmiViewModel = obtainViewModel(this@ActivityBMI)
        setFields(bmiViewModel)

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
    private fun setFields(bmiViewModel: BMIViewModel){
        lifecycleScope.launch(Dispatchers.IO){
            val height = bmiViewModel.getUser()?.height.toString()
            val weight = bmiViewModel.getUser()?.weight.toString()
            binding.etHeight.text = Editable.Factory.getInstance().newEditable(height)
            binding.etWeight.text = Editable.Factory.getInstance().newEditable(weight)
        }
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

                bmi < 23.9 -> {
                    bmiCategory = "Normal weight"
                    imgResource = R.drawable.normal_weight
                }

                bmi < 32.9 -> {
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

    private fun obtainViewModel(activity: AppCompatActivity): BMIViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(BMIViewModel::class.java)
    }
}