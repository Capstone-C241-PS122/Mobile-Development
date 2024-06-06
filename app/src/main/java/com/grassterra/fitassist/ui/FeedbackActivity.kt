package com.grassterra.fitassist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.grassterra.fitassist.databinding.ActivityFeedbackBinding

class FeedbackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedbackBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}