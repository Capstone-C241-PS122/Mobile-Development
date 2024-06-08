package com.grassterra.fitassist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.grassterra.fitassist.databinding.ActivityCalculateNutritionBinding
import java.text.DecimalFormat
import java.util.Locale

class ActivityCalculateNutrition : AppCompatActivity() {
    private lateinit var binding: ActivityCalculateNutritionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculateNutritionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonCalculate.setOnClickListener{
            resultCalculate()

        }
    }
    private fun resultCalculate() {
        var kalori100gram = 0.0
        var protein100gram = 0.0
        var karbohidrat100gram = 0.0
        var lemak100gram = 0.0
        val df = DecimalFormat("#.##")
        val editTextFood = binding.editTextFood.text.toString()

        when (editTextFood.toLowerCase(Locale.getDefault())) {
            "dada ayam" -> {
                kalori100gram = 195.0
                protein100gram  = 30.0
                karbohidrat100gram = 0.0
                lemak100gram = 8.0
            }
            "wortel" -> {
                kalori100gram  = 41.0
                protein100gram = 0.93
                karbohidrat100gram = 9.58
                lemak100gram = 0.24
            }

            else -> {
                binding.textViewResult.text = "Makanan tidak ditemukan."
                return
            }
        }
        val makananpergram = binding.editTextWeight.text.toString().toInt()
        val dibagiPergramKalori = kalori100gram / 100
        val dibagiPergramProtein = protein100gram / 100
        val dibagiPergramLemak = lemak100gram / 100
        val dibagiPergramKarbohidrat = karbohidrat100gram / 100
        val hasilkalori = makananpergram * dibagiPergramKalori
        val hasilprotein = makananpergram * dibagiPergramProtein
        val hasilkarbohidrat = makananpergram * dibagiPergramKarbohidrat
        val hasillemak = makananpergram * dibagiPergramLemak

        val formattedKalori = df.format(hasilkalori)
        val formattedProtein = df.format(hasilprotein)
        val formattedKarbohidrat = df.format(hasilkarbohidrat)
        val formattedLemak = df.format(hasillemak)

        binding.textViewResult.text = "===== Informasi Gizi =====\n" +
                "Makanan: $editTextFood\n" +
                "Berat: $makananpergram gram\n" +
                "Kalori: $formattedKalori Kalori\n" +
                "Protein: $formattedProtein gram\n" +
                "Karbohidrat: $formattedKarbohidrat gram\n" +
                "Lemak: $formattedLemak gram"

        binding.editTextFood.text.clear()
        binding.editTextWeight.text.clear()
    }


}