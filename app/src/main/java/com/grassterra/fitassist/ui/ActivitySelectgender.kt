package com.grassterra.fitassist.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.grassterra.fitassist.ActivityInputHeight

import com.grassterra.fitassist.databinding.ActivitySelectgenderBinding
import com.grassterra.fitassist.R


class ActivitySelectgender : AppCompatActivity() {
    private var isZoomedIn = false
    private lateinit var binding : ActivitySelectgenderBinding
    private val handler = Handler()
    private val typingDelay: Long = 100
    private val blinkDelay: Long = 250
    private val fullText = "What is your gender? "
    private var isCursorBlinking = false
    private lateinit var cursorDrawable: Drawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySelectgenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cursorDrawable = resources.getDrawable(R.drawable.drawable_indicator, null)
        startBlinkingCursor()
        typeText(fullText)
        val toggleButtonMan: ToggleButton = binding.btnSelectman
        val bottomMan: ImageView = binding.bottomMan
        val backgroundMan: ImageView = binding.idbackgroundMan
        val textSelectedMan: TextView = binding.textSelectedMan
        val bottomWoman: ImageView = binding.bottomWoman
        val toggleButtonWoman: ToggleButton = binding.toggleButtonWoman
        val backgroundWoman: ImageView = binding.idbackgroundWoman
        val textSelectedWoman: TextView = binding.textSelectedWoman
        toggleButtonMan.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (!isZoomedIn) {
                    buttonView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in))
                    isZoomedIn = true
                    bottomMan.visibility = View.VISIBLE
                    backgroundMan.visibility = View.VISIBLE
                    textSelectedMan.visibility = View.VISIBLE
                    if (toggleButtonWoman.isChecked) {
                        toggleButtonWoman.isChecked = false
                        backgroundWoman.visibility = View.INVISIBLE
                        bottomWoman.visibility = View.INVISIBLE
                        textSelectedWoman.visibility = View.INVISIBLE
                    }
                }
                val gender = getSelectedGender(toggleButtonMan, toggleButtonWoman)
                Log.d("YourActivity", "Selected Gender: $gender")
            } else {
                if (isZoomedIn) {
                    buttonView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_out))
                    isZoomedIn = false
                    backgroundMan.visibility = View.INVISIBLE
                    bottomMan.visibility = View.INVISIBLE
                    textSelectedMan.visibility = View.INVISIBLE
                }
            }
        }

        toggleButtonWoman.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (!isZoomedIn) {
                    buttonView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in))
                    isZoomedIn = true
                    bottomWoman.visibility = View.VISIBLE
                    backgroundWoman.visibility = View.VISIBLE
                    textSelectedWoman.visibility = View.VISIBLE
                    if (toggleButtonMan.isChecked) {
                        toggleButtonMan.isChecked = false
                        backgroundMan.visibility = View.INVISIBLE
                        bottomMan.visibility = View.INVISIBLE
                        textSelectedMan.visibility = View.INVISIBLE
                    }
                }
                val gender = getSelectedGender(toggleButtonMan, toggleButtonWoman)
                Log.d("YourActivity", "Selected Gender: $gender")
            } else {
                if (isZoomedIn) {
                    buttonView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_out))
                    isZoomedIn = false
                    backgroundWoman.visibility = View.INVISIBLE
                    bottomWoman.visibility = View.INVISIBLE
                    textSelectedWoman.visibility = View.INVISIBLE
                }
            }
        }
        binding.btnNext.setOnClickListener {
           NavigateNextPage(this)
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
    private fun getSelectedGender(toggleButtonMan: ToggleButton, toggleButtonWoman: ToggleButton): String {
        return if (toggleButtonMan.isChecked) {
            "Man"
        } else if (toggleButtonWoman.isChecked) {
            "Woman"
        } else {
            "Unknown"
        }
    }
    private fun NavigateNextPage(context:Context){
        val intent = Intent(context,ActivityInputHeight::class.java)
        context.startActivity(intent)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

}