package com.grassterra.fitassist.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.grassterra.fitassist.ActivityInputWeight
import com.grassterra.fitassist.R
import com.grassterra.fitassist.database.user.Userdata
import com.grassterra.fitassist.databinding.ActivitySelectgenderBinding

class ActivitySelectgender : AppCompatActivity() {
    private var isZoomedIn = false
    private lateinit var binding : ActivitySelectgenderBinding
    private val handler = Handler()
    private val typingDelay: Long = 100
    private val blinkDelay: Long = 250
    private val fullText = "What is your gender? \uD83D\uDC68 or \uD83D\uDC69 "
    private var isCursorBlinking = false
    private lateinit var cursorDrawable: Drawable
    private lateinit var userData: Userdata

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySelectgenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userData = intent.getParcelableExtra("userdata") ?: Userdata()

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
            onToggleButtonChecked(buttonView, isChecked, toggleButtonWoman, bottomMan, backgroundMan, textSelectedMan, bottomWoman, backgroundWoman, textSelectedWoman)
        }

        toggleButtonWoman.setOnCheckedChangeListener { buttonView, isChecked ->
            onToggleButtonChecked(buttonView, isChecked, toggleButtonMan, bottomWoman, backgroundWoman, textSelectedWoman, bottomMan, backgroundMan, textSelectedMan)
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
    private fun onToggleButtonChecked(
        buttonView: CompoundButton,
        isChecked: Boolean,
        otherToggleButton: ToggleButton,
        visibleView1: View,
        visibleView2: View,
        visibleView3: View,
        invisibleView1: View,
        invisibleView2: View,
        invisibleView3: View
    ) {
        if (isChecked) {
            if (!isZoomedIn) {
                buttonView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in))
                isZoomedIn = true
                visibleView1.visibility = View.VISIBLE
                visibleView2.visibility = View.VISIBLE
                visibleView3.visibility = View.VISIBLE
                if (otherToggleButton.isChecked) {
                    otherToggleButton.isChecked = false
                    invisibleView1.visibility = View.INVISIBLE
                    invisibleView2.visibility = View.INVISIBLE
                    invisibleView3.visibility = View.INVISIBLE
                }
            }
            val gender = getSelectedGender(binding.btnSelectman, binding.toggleButtonWoman)
            Log.d("YourActivity", "Selected Gender: $gender")
        } else {
            if (isZoomedIn) {
                buttonView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_out))
                isZoomedIn = false
                visibleView2.visibility = View.INVISIBLE
                visibleView1.visibility = View.INVISIBLE
                visibleView3.visibility = View.INVISIBLE
            }
        }
    }
    private fun getSelectedGender(toggleButtonMan: ToggleButton, toggleButtonWoman: ToggleButton): String {
        return if (toggleButtonMan.isChecked) {
            userData.gender = true
            "Man"
        } else if (toggleButtonWoman.isChecked) {
            userData.gender = false
            "Woman"
        } else {
            "Unknown"
        }
    }
    private fun NavigateNextPage(context:Context){
        val intent = Intent(context, ActivityInputWeight::class.java)
        intent.putExtra("userdata",userData)
        context.startActivity(intent)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

}