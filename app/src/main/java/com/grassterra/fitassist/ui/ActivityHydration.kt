package com.grassterra.fitassist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.grassterra.fitassist.databinding.ActivityHydrationBinding

class ActivityHydration : AppCompatActivity() {
    private lateinit var binding: ActivityHydrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHydrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
}