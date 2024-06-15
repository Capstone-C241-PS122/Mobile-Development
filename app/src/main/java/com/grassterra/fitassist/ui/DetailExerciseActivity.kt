package com.grassterra.fitassist.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.grassterra.fitassist.database.history.HistoryItem
import com.grassterra.fitassist.databinding.ActivityDetailExerciseBinding
import com.grassterra.fitassist.helper.ParcelableMap
import com.grassterra.fitassist.helper.Resource
import com.grassterra.fitassist.helper.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailExerciseBinding
    private var isTopBarVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val detailExerciseViewModel = obtainViewModel(this@DetailExerciseActivity)
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
        setupView(detailExerciseViewModel)
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

    private fun setupView(detailExerciseViewModel: DetailExerciseViewModel) {
        val imageUriString = intent.getStringExtra("imageUri")
        val imageUri = Uri.parse(imageUriString)
        binding.resultImage.setImageURI(imageUri)

        val parcelableMap = intent.getParcelableExtra<ParcelableMap>("result")
        parcelableMap?.map?.let { map ->
            val stringBuilder = StringBuilder()
            var maxLabel: String? = null
            var maxValue: Float = Float.MIN_VALUE

            map.forEach { (label, value) ->
                stringBuilder.append("$label: $value \n")
                if (value > maxValue) {
                    maxValue = value
                    maxLabel = label
                }
            }

            maxLabel?.let { maxLabel ->
                detailExerciseViewModel.postLabel(maxLabel).observe(this) { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val response = resource.data.get(0)
                            Log.d("DetailExerciseActivity", "Response: $response")
                            binding.idNameLabel.text = response.nameEquipment
                            binding.description.text = response.description
                            binding.nameExercise.text = response.nameExercise
                            binding.bodyPart.text = response.bodypart
                            val videoUrl = response.urlVideo

                            // Set up VideoView
                            if (!videoUrl.isNullOrEmpty()) {
                                val uri = Uri.parse(videoUrl)
                                binding.videoView.setMediaController(
                                    android.widget.MediaController(
                                        this
                                    )
                                )
                                binding.videoView.setVideoURI(uri)
                                binding.videoView.setOnPreparedListener { mediaPlayer ->
                                    mediaPlayer.start()
                                }
                            } else {
                                showToast("Video URL is empty or invalid")
                            }
                        }
                        is Resource.Error -> {
                            // Handle the error
                            Log.e("DetailExerciseActivity", "Error: ${resource.errorMessage}")
                            showToast("Error: ${resource.errorMessage}")
                        }
                    }
                }
            }
        }

        saveExercise(imageUri, detailExerciseViewModel)
    }
    private fun showToast(message: String) {
        Toast.makeText(this@DetailExerciseActivity, message, Toast.LENGTH_SHORT).show()
    }


    fun getCurrentDateTime(): String {
        val date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(date)
    }

    fun saveExercise(imageUri: Uri, detailExerciseViewModel: DetailExerciseViewModel){
        val equipment = binding.idNameLabel.text.toString()
        val bodypart = binding.bodyPart.text.toString()
        val img = imageUri.toString()
        val exercise = binding.nameExercise.text.toString()
        val date = getCurrentDateTime() // You need to implement this function to get the current date/time as a String
        val historyItem = HistoryItem(
            equipment = equipment,
            bodypart = bodypart,
            imageuri = img,
            exercise = exercise,
            date = date
        )

        lifecycleScope.launch(Dispatchers.IO) {
            detailExerciseViewModel.saveHistory(historyItem)
            Log.d("SUC", historyItem.toString())
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailExerciseViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailExerciseViewModel::class.java)
    }
}