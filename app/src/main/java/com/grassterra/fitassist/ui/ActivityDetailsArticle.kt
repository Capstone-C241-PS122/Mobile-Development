package com.grassterra.fitassist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.grassterra.fitassist.databinding.ActivityDetailsArticleBinding

class ActivityDetailsArticle : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}