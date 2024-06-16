package com.grassterra.fitassist.ui.article

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.grassterra.fitassist.databinding.ActivityDetailsArticleBinding
import com.grassterra.fitassist.helper.Resource
import com.grassterra.fitassist.helper.ViewModelFactory
import com.grassterra.fitassist.ui.exercise.DetailExerciseViewModel

class ActivityDetailsArticle : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailsArticleViewModel = obtainViewModel(this@ActivityDetailsArticle)
        setupView(detailsArticleViewModel)
    }

    private fun setupView(detailsArticleViewModel: DetailsArticleViewModel){
        val id = intent.getStringExtra("id")?.toInt()
        if (id != null) {
            detailsArticleViewModel.setArticle(id).observe(this){resource ->
                when (resource){
                    is Resource.Success -> {
                        val resp = resource.data.article
                        binding.tvTitle.text = resp.title
                        binding.tvDesc.text = resp.description
                        binding.tvBodyPart.text = resp.bodyPart
                        binding.tvEquipment.text = resp.equipment
                        binding.tvType.text = resp.type
                        binding.iVLevel.text = resp.level
                    }
                    is Resource.Error -> {
                        Log.e("DetailExerciseActivity", "Error: ${resource.errorMessage}")
                        showToast("Error: ${resource.errorMessage}")
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@ActivityDetailsArticle, message, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailsArticleViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailsArticleViewModel::class.java)
    }
}