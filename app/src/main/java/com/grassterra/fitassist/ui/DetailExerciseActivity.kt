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
        val result = intent.getStringExtra("result")
        binding.textDescriptionLabel.setText(result)
        Log.d("SetupImage", "Image URI: $imageUriString")
        Log.d("SetupImage", "Result: $result")
    }
}