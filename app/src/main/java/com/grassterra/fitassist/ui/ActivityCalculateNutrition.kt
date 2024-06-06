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
        var makanan: String
        var kaloripergram = 0.0
        var proteinpergram = 0.0
        var karbohidratpergram = 0.0
        var lemakpergram = 0.0
        val df = DecimalFormat("#.##")
        val editTextFood = binding.editTextFood.text.toString()

        when (editTextFood.toLowerCase(Locale.getDefault())) {
            "dada ayam" -> {
                kaloripergram = 1.95
                proteinpergram = 0.3
                karbohidratpergram = 0.0
                lemakpergram = 0.08
            }
            "wortel" -> {
                kaloripergram = 0.41
                proteinpergram = 0.0093
                karbohidratpergram = 0.0958
                lemakpergram = 0.0024
            }
            "dada ayam tanpa kulit dan tulang dengan dikukus" -> {
                kaloripergram = 1.45
                proteinpergram = 0.29
                karbohidratpergram = 0.0858
                lemakpergram = 0.03
            }
            "bayam" -> {
                kaloripergram = 0.23
                proteinpergram = 0.0286
                karbohidratpergram = 0.0363
                lemakpergram = 0.0039
            }
            "udang" -> {
                kaloripergram = 1.44
                proteinpergram = 0.2759
                karbohidratpergram = 0.0124
                lemakpergram = 0.0235
            }
            else -> {
                binding.textViewResult.text = "Makanan tidak ditemukan."
                return
            }
        }

        val makananpergram = binding.editTextWeight.text.toString().toInt()
        val hasilkalori = makananpergram * kaloripergram
        val hasilprotein = makananpergram * proteinpergram
        val hasilkarbohidrat = makananpergram * karbohidratpergram
        val hasillemak = makananpergram * lemakpergram

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