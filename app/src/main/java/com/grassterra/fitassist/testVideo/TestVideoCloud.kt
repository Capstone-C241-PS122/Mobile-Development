package com.grassterra.fitassist.testVideo

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.grassterra.fitassist.databinding.ActivityTestVideoCloudBinding

class TestVideoCloud : AppCompatActivity() {
    private lateinit var binding: ActivityTestVideoCloudBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestVideoCloudBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding.videoView)
        binding.videoView.setMediaController(mediaController)
        val videoUrl = "https://storage.googleapis.com/bucket_fitassist/Cable_Machine_Tutorial.mp4"
        val uri = Uri.parse(videoUrl)
        binding.videoView.setVideoURI(uri)
        binding.videoView.start()
    }
}