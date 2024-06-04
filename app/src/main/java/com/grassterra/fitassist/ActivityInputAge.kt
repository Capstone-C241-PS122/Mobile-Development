package com.grassterra.fitassist

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.grassterra.fitassist.databinding.ActivityInputAgeBinding

class ActivityInputAge : AppCompatActivity() {
    private lateinit var binding:ActivityInputAgeBinding
    private val handler = Handler()
    private val typingDelay: Long = 100
    private val blinkDelay: Long = 250
    private val fullText = "How old are you? "
    private var isCursorBlinking = false
    private lateinit var cursorDrawable: Drawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputAgeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cursorDrawable = resources.getDrawable(R.drawable.drawable_indicator, null)
        startBlinkingCursor()
        binding.numberPicker.apply {
            minValue = 0
            maxValue = 100
            value = 18
            setOnValueChangedListener { _, _, newVal ->
            }
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
}