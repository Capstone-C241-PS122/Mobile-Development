package com.grassterra.fitassist

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.grassterra.fitassist.databinding.ActivitySplashScreenBinding
import com.grassterra.fitassist.ui.mainMenu.MainMenu

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fadeInLogo()
        fadeInText()
        changeTextColor()
    }
    private fun changeTextColor() {
        val spannable = SpannableString(binding.idTextSplashScreen.text)
        val colorFit = ContextCompat.getColor(this, android.R.color.white)
        val colorAssist = ContextCompat.getColor(this, R.color.colorPrimary)

        spannable.setSpan(ForegroundColorSpan(colorFit), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(colorAssist), 3, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.idTextSplashScreen.text = spannable
    }
    private fun fadeInText() {
        binding.idTextSplashScreen.alpha = 0f

        ViewCompat.animate(binding.idTextSplashScreen)
            .alpha(1f)
            .setDuration(3000)
            .withEndAction {
                Handler(Looper.getMainLooper()).postDelayed({
                    fadeOutTextAndNavigate()
                }, 5000)
            }
            .start()
    }

    private fun fadeOutTextAndNavigate() {
        ViewCompat.animate(binding.idTextSplashScreen)
            .alpha(0f)
            .setDuration(1000)
            .withEndAction {
                val intent = Intent(this@SplashScreen, MainMenu::class.java)
                startActivity(intent)
                finish()
            }
            .start()
    }
    private fun fadeInLogo() {
        binding.logoSplashScreen.alpha = 0f

        ViewCompat.animate(binding.logoSplashScreen)
            .alpha(1f)
            .setDuration(3000)
            .withEndAction {
                Handler(Looper.getMainLooper()).postDelayed({
                    fadeOutLogo()
                }, 5000)
            }
            .start()
    }
    private fun fadeOutLogo() {
        ViewCompat.animate(binding.logoSplashScreen)
            .alpha(0f)
            .setDuration(1000)
            .withEndAction {
                val intent = Intent(this@SplashScreen, MainMenu::class.java)
                startActivity(intent)
                finish()
            }
            .start()
    }
}