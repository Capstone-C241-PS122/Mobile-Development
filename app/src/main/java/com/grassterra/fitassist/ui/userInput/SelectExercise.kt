package com.grassterra.fitassist.ui.userInput

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.grassterra.fitassist.R
import com.grassterra.fitassist.database.user.Userdata
import com.grassterra.fitassist.databinding.ActivitySelectExerciseBinding
import com.grassterra.fitassist.helper.ViewModelFactory
import com.grassterra.fitassist.ui.fragments.LottieLoadingFragment
import com.grassterra.fitassist.ui.mainMenu.MainMenu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SelectExercise : AppCompatActivity() {
    private  lateinit var binding: ActivitySelectExerciseBinding
    private val handler = Handler()
    private val typingDelay: Long = 100
    private val blinkDelay: Long = 250
    private val fullText = "Select your target body part? \uD83C\uDFCB\uFE0F\u200D♂\uFE0F"
    private var isCursorBlinking = false
    private lateinit var cursorDrawable: Drawable
    private lateinit var loadingFragment: LottieLoadingFragment
    private lateinit var userData: Userdata

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
        val selectExerciseViewModel = obtainViewModel(this@SelectExercise)
        userData = intent.getParcelableExtra("userdata") ?: Userdata()
        selectExerciseViewModel.clear()
        cursorDrawable = resources.getDrawable(R.drawable.drawable_indicator, null)
        toggleButtons.forEach { button ->
            button.setOnCheckedChangeListener { _, isChecked ->
                updateTextColor(button, isChecked)
                if (isChecked) {
                    Log.d("ToggleButton", "${button.text} is checked")
                }
                lifecycleScope.launch(Dispatchers.IO) {
                    selectExerciseViewModel.toggle(button.text.toString())
                    Log.d("ToggleButton", selectExerciseViewModel.getAll().toString())
                }
            }
            updateTextColor(button, button.isChecked)
        }
        binding.btnNext.setOnClickListener{
            showLoadingAndNavigate(this)
        }
        loadingFragment = LottieLoadingFragment()
        startBlinkingCursor()
        typeText(fullText)
    }
    private fun showLoadingAndNavigate(context: Context) {
        binding.apply {
            btnChest.visibility = View.INVISIBLE
            btnForearms.visibility = View.INVISIBLE
            btnGlutes.visibility = View.INVISIBLE
            btnHamstrings.visibility = View.INVISIBLE
            btnLats.visibility = View.INVISIBLE
            btnLowerBack.visibility = View.INVISIBLE
            btnMiddleBack.visibility = View.INVISIBLE
            btnQuadriceps.visibility = View.INVISIBLE
            btnShoulders.visibility = View.INVISIBLE
            btnTriceps.visibility = View.INVISIBLE
            btnNext.visibility = View.INVISIBLE
            textView.visibility = View.INVISIBLE
            lottieAnimationView.visibility = View.INVISIBLE
            loadingFragmentContainer.visibility = View.VISIBLE
        }
        supportFragmentManager.commit {
            replace(R.id.loading_fragment_container, loadingFragment)
            setReorderingAllowed(true)
            addToBackStack(null)

        }
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.loadingFragmentContainer.startAnimation(fadeInAnimation)
        supportFragmentManager.executePendingTransactions()
        loadingFragment.startAnimation()
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            NavigateNextPage(context)
        }
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

    private fun obtainViewModel(activity: AppCompatActivity): SelectExerciseViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(SelectExerciseViewModel::class.java)
    }

    private fun NavigateNextPage(context: Context) {
        loadingFragment.stopAnimation()
        binding.loadingFragmentContainer.visibility = View.GONE
        val intent = Intent(context, MainMenu::class.java)
        intent.putExtra("flag", true)
        intent.putExtra("userdata",userData)
        startActivity(intent)
        finish()
    }
}