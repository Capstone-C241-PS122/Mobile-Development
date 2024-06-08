package com.grassterra.fitassist.ui

import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.grassterra.fitassist.databinding.ActivityHydrationBinding
import com.grassterra.fitassist.helper.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityHydration : AppCompatActivity() {
    private lateinit var binding: ActivityHydrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHydrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bmiViewModel = obtainViewModel(this@ActivityHydration)
        setFields(bmiViewModel)

        binding.btnCalculate.setOnClickListener{
            resultHydration()
        }
    }
    private fun resultHydration(){
        val weight = binding.etWeight.text.toString().toDoubleOrNull() ?: 0.0
        val faktoKonversi = 37
        val hydration = weight * faktoKonversi
        val roundHydration = hydration.toInt()
        binding.resultTextView.text = "$roundHydration ml/day or $hydration liter "
    }

    private fun setFields(bmiViewModel: BMIViewModel) {
        lifecycleScope.launch(Dispatchers.IO) {
            val weight = bmiViewModel.getUser()?.weight.toString()
            withContext(Dispatchers.Main) {
                binding.etWeight.text = Editable.Factory.getInstance().newEditable(weight)
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): BMIViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(BMIViewModel::class.java)
    }
}