package com.grassterra.fitassist.ui

import android.net.Uri
import android.os.Bundle
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
        setupImage()
    }

    private fun setupImage(){
        val imageUri = Uri.parse(intent.getStringExtra("imageUri"))
        binding.resultImage.setImageURI(imageUri)

        val result = intent.getStringExtra("result")
        binding.textDescriptionLabel.setText(result)
    }
}