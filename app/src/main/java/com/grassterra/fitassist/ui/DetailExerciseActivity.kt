package com.grassterra.fitassist.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.grassterra.fitassist.databinding.ActivityDetailExerciseBinding
import com.grassterra.fitassist.helper.ParcelableMap

class DetailExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailExerciseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener{
            BackMainMenu(this)
        }
        setupImage()
    }
    private fun BackMainMenu(context: Context){
        val intent = Intent(context,MainMenu::class.java)
        context.startActivity(intent)
    }

    private fun setupImage() {
        val imageUriString = intent.getStringExtra("imageUri")
        val imageUri = Uri.parse(imageUriString)
        binding.resultImage.setImageURI(imageUri)

//        val result = intent.getStringExtra("result")
//        binding.textDescriptionLabel.setText(result)

        val parcelableMap = intent.getParcelableExtra<ParcelableMap>("result")
        parcelableMap?.map?.let {
            // Process the received map
            val stringBuilder = StringBuilder()
            var maxLabel: String? = null
            var maxValue: Float = Float.MIN_VALUE

            it.forEach { (label, value) ->
                stringBuilder.append("$label: $value \n")
                if (value > maxValue) {
                    maxValue = value
                    maxLabel = label
                }
            }
            binding.textDescriptionLabel.setText(stringBuilder)
            binding.idNameLabel.setText(maxLabel)
        }
    }
}