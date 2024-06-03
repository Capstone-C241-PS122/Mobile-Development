package com.grassterra.fitassist.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AlphaAnimation
import androidx.core.view.isVisible
import com.grassterra.fitassist.R
import com.grassterra.fitassist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeStatusBarColor()
        fadeInAnimation(binding.imageFirstpage)
        fadeInAnimation(binding.DescriptionText)
        fadeInAnimation(binding.textWelcome)
        fadeInAnimation(binding.btnGetStarted)
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val density = resources.displayMetrics.density
        val buttonLayoutParams = binding.btnGetStarted.layoutParams as ViewGroup.LayoutParams
        when {
            screenWidth < 740 -> {
                buttonLayoutParams.width = (275 * density).toInt()
                buttonLayoutParams.height = (65 * density).toInt()
                binding.DescriptionText.textSize = 16f
                binding.textWelcome.textSize = 22f
                binding.btnGetStarted.textSize = 17f
            }
            screenWidth < 1100 -> {
                // Medium screen
                buttonLayoutParams.width = (300 * density).toInt()
                buttonLayoutParams.height = (68 * density).toInt()
                binding.btnGetStarted.textSize = 18f
                binding.DescriptionText.textSize = 16f
                binding.textWelcome.textSize = 22f
            }
            else -> {
                // Large screen
                binding.btnGetStarted.textSize = 20f
                binding.DescriptionText.textSize = 17f
                binding.textWelcome.textSize = 22f
                buttonLayoutParams.width = (350 * density).toInt()
                buttonLayoutParams.height = (75 * density).toInt()
            }
        }

        binding.btnGetStarted.layoutParams = buttonLayoutParams
    }
    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.statusBarColor = resources.getColor(R.color.background, theme)
        }
    }
    private fun fadeInAnimation(view: View) {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 3000
        view.startAnimation(fadeIn)
        view.isVisible = true
    }

}