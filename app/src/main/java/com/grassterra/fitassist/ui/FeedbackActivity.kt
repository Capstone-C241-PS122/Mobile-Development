package com.grassterra.fitassist.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.grassterra.fitassist.databinding.ActivityFeedbackBinding

class FeedbackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedbackBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener{
            GoToBack(this)
        }
    }
    private fun GoToBack(context: Context){
        val intent = Intent(context,MainMenu::class.java)
        context.startActivity(intent)
    }
}