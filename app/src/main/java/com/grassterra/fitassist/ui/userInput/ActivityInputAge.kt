package com.grassterra.fitassist.ui.userInput

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import com.grassterra.fitassist.R
import com.grassterra.fitassist.database.user.Userdata
import com.grassterra.fitassist.databinding.ActivityInputAgeBinding

class ActivityInputAge : AppCompatActivity() {
    private lateinit var binding:ActivityInputAgeBinding
    private val handler = Handler()
    private val typingDelay: Long = 100
    private val blinkDelay: Long = 250
    private val fullText = "How old are you?"
    private var isCursorBlinking = false
    private lateinit var cursorDrawable: Drawable
    private lateinit var userData: Userdata
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputAgeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userData = intent.getParcelableExtra("userdata") ?: Userdata()
        cursorDrawable = resources.getDrawable(R.drawable.drawable_indicator, null)
        startBlinkingCursor()
        binding.numberPicker.apply {
            minValue = 0
            maxValue = 100
            value = 18
            setOnValueChangedListener { _, _, newVal ->
                userData.age = newVal
            }
            setNumberPickerTextColor(this, Color.WHITE)
            setNumberPickerDividerColor(this, Color.WHITE)
        }
        binding.btnNext.setOnClickListener {
            nullCheck(userData)
            NavigateNextPage(this)
        }
        typeText(fullText)
    }
    private fun setNumberPickerTextColor(numberPicker: NumberPicker, color: Int) {
        try {
            val fields = NumberPicker::class.java.declaredFields
            for (field in fields) {
                if (field.name == "mSelectorWheelPaint") {
                    field.isAccessible = true
                    val paint = field.get(numberPicker) as Paint
                    paint.color = color
                    numberPicker.invalidate()
                }
                if (field.name == "mInputText") {
                    field.isAccessible = true
                    val inputText = field.get(numberPicker) as EditText
                    inputText.setTextColor(color)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun setNumberPickerDividerColor(numberPicker: NumberPicker, color: Int) {
        try {
            val fields = NumberPicker::class.java.declaredFields
            for (field in fields) {
                if (field.name == "mSelectionDivider") {
                    field.isAccessible = true
                    field.set(numberPicker, ColorDrawable(color))
                    numberPicker.invalidate()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    private fun nullCheck(userData: Userdata){
        if (userData.age == null){
            userData.age = 18
        }
    }
    private fun NavigateNextPage(context: Context) {
        val intent = Intent(context, SelectExercise::class.java)
        intent.putExtra("userdata",userData)
        context.startActivity(intent)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }
}