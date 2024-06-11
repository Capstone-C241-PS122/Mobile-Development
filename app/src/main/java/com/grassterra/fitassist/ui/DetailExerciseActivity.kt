package com.grassterra.fitassist.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.grassterra.fitassist.databinding.ActivityDetailExerciseBinding
import com.grassterra.fitassist.helper.ParcelableMap

class DetailExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailExerciseBinding
    private var isTopBarVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener{
            BackMainMenu(this)
        }
        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY > 0 && binding.topBar.visibility != View.VISIBLE) {
                fadeInView(binding.topBar)
            } else if (scrollY == 0 && binding.topBar.visibility == View.VISIBLE) {
                fadeOutView(binding.topBar)
            }
        })
        setupImage()
    }
    private fun fadeInView(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 500
        view.visibility = View.VISIBLE
        view.startAnimation(anim)
    }

    private fun fadeOutView(view: View) {
        val anim = AlphaAnimation(1.0f, 0.0f)
        anim.duration = 500
        view.startAnimation(anim)
        view.visibility = View.INVISIBLE
    }
    private fun BackMainMenu(context: Context){
        val intent = Intent(context,MainMenu::class.java)
        context.startActivity(intent)
    }

    private fun setupImage() {
        val imageUriString = intent.getStringExtra("imageUri")
        val imageUri = Uri.parse(imageUriString)
        binding.resultImage.setImageURI(imageUri)

//        val result = intent.getStringExtra("result")
//        binding.textDescriptionLabel.setText(result)

        val parcelableMap = intent.getParcelableExtra<ParcelableMap>("result")
        parcelableMap?.map?.let {
            // Process the received map
            val stringBuilder = StringBuilder()
            var maxLabel: String? = null
            var maxValue: Float = Float.MIN_VALUE

            it.forEach { (label, value) ->
                stringBuilder.append("$label: $value \n")
                if (value > maxValue) {
                    maxValue = value
                    maxLabel = label
                }
            }
            binding.textDescriptionLabel.setText(stringBuilder)
            binding.idNameLabel.setText(maxLabel)
        }
    }
}