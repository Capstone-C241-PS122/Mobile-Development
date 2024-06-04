package com.grassterra.fitassist

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler

import androidx.appcompat.app.AppCompatActivity
import com.grassterra.fitassist.databinding.ActivityInputHeightBinding

class ActivityInputHeight : AppCompatActivity() {
    private lateinit var binding: ActivityInputHeightBinding
    private val handler = Handler()
    private val typingDelay: Long = 100
    private val blinkDelay: Long = 250
    private val fullText = "What is your height? "
    private var isCursorBlinking = false
    private lateinit var cursorDrawable: Drawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputHeightBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cursorDrawable = resources.getDrawable(R.drawable.drawable_indicator, null)
        startBlinkingCursor()
        binding.numberPicker.apply {
            minValue = 0
            maxValue = 300
            value = 160
            setOnValueChangedListener { _, _, newVal ->
            }
        }
        binding.btnNext.setOnClickListener {
            NavigateNextPage(this)
        }

        typeText(fullText)
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
        val intent = Intent(context,ActivityInputAge::class.java)
        context.startActivity(intent)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }
}