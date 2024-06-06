package com.grassterra.fitassist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.grassterra.fitassist.databinding.ActivityLibraryVideoBinding

class LibraryVideo : AppCompatActivity() {
    private lateinit var binding: ActivityLibraryVideoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}