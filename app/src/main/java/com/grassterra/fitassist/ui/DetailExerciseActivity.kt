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
<<<<<<< HEAD:app/src/main/java/com/grassterra/fitassist/DetailExerciseActivity.kt
=======

        setupImage()
    }

    private fun setupImage(){
        val imageUri = Uri.parse(intent.getStringExtra("imageUri"))
        binding.resultImage.setImageURI(imageUri)

        val result = intent.getStringExtra("result")
        binding.textDescriptionLabel.setText(result)
>>>>>>> 35e99518ec1b829ace1d27d6ff155629d6092f45:app/src/main/java/com/grassterra/fitassist/ui/DetailExerciseActivity.kt
    }
}