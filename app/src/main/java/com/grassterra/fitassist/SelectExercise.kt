package com.grassterra.fitassist

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.grassterra.fitassist.databinding.ActivitySelectExerciseBinding
import com.grassterra.fitassist.ui.MainMenu

class SelectExercise : AppCompatActivity() {
    private  lateinit var binding: ActivitySelectExerciseBinding
    private val handler = Handler()
    private val typingDelay: Long = 100
    private val blinkDelay: Long = 250
    private val fullText = "Select your target body part? \uD83C\uDFCB\uFE0F\u200D♂\uFE0F"
    private var isCursorBlinking = false
    private lateinit var cursorDrawable: Drawable
    private val toggleButtons: List<ToggleButton> by lazy {
        listOf(
            binding.btnChest, binding.btnForearms, binding.btnGlutes,
            binding.btnHamstrings, binding.btnLats, binding.btnLowerBack,
            binding.btnMiddleBack, binding.btnQuadriceps, binding.btnShoulders,
            binding.btnTriceps
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cursorDrawable = resources.getDrawable(R.drawable.drawable_indicator, null)
        toggleButtons.forEach { button ->
            button.setOnCheckedChangeListener { _, isChecked ->
                updateTextColor(button, isChecked)
                if (isChecked) {
                    Log.d("ToggleButton", "${button.text} is checked")
                }
            }
            updateTextColor(button, button.isChecked)
        }
        binding.btnNext.setOnClickListener{
            NavigateNextPage(this)
        }


        startBlinkingCursor()
        typeText(fullText)
    }

    private fun updateTextColor(button: ToggleButton, isChecked: Boolean) {
        val color = if (isChecked) {
            ContextCompat.getColor(this, R.color.background)
        } else {
            ContextCompat.getColor(this, R.color.white)
        }
        button.setTextColor(color)
    }
    private fun typeText(text: String, index: Int = 0) {
        if (index < text.length) {
            binding.textView.text = text.substring(0, index + 1)
            handler.postDelayed({ typeText(text, index + 1) }, typingDelay)
        }
    }

    private fun startBlinkingCursor() {
        if (!isCursorBlinking) {
            isCursorBlinking = true
            binding.textView.setCompoundDrawablesWithIntrinsicBounds(null, null, cursorDrawable, null)
            handler.post(object : Runnable {
                override fun run() {
                    binding.textView.compoundDrawables[2]?.alpha = if (binding.textView.compoundDrawables[2]?.alpha == 0) 255 else 0
                    handler.postDelayed(this, blinkDelay)
                }
            })
        }
    }
    private fun NavigateNextPage(context: Context) {
        val intent = Intent(context,MainMenu::class.java)
        context.startActivity(intent)
    }
}