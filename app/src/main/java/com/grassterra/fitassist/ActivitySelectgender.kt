package com.grassterra.fitassist

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivitySelectgender : AppCompatActivity() {
    private var isZoomedIn = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_selectgender)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val toggleButtonMan: ToggleButton = findViewById(R.id.btnSelectman)
        val bottomMan: ImageView = findViewById(R.id.bottomMan)
        val backgroundMan: ImageView = findViewById(R.id.idbackgroundMan)
        val textSelectedMan: TextView = findViewById(R.id.textSelectedMan)
        val bottomWoman: ImageView = findViewById(R.id.bottomWoman)
        val toggleButtonWoman: ToggleButton = findViewById(R.id.toggleButtonWoman)
        val backgroundWoman: ImageView = findViewById(R.id.idbackgroundWoman)
        val textSelectedWoman: TextView = findViewById(R.id.textSelectedWoman)

        toggleButtonMan.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (!isZoomedIn) {
                    buttonView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in))
                    isZoomedIn = true
                    bottomMan.visibility = View.VISIBLE
                    backgroundMan.visibility = View.VISIBLE
                    textSelectedMan.visibility = View.VISIBLE
                    toggleButtonWoman.isChecked = false
                }
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
                    toggleButtonMan.isChecked = false
                }
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
    }
}