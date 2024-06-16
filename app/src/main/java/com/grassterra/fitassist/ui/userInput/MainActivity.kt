package com.grassterra.fitassist.ui.userInput

import android.content.Context
import android.content.Intent
import android.graphics.Matrix
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity
import com.grassterra.fitassist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val videoUrl = "https://storage.googleapis.com/bucket_fitassist/video_background.mp4"
        binding.textureView.post {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(this@MainActivity, Uri.parse(videoUrl))
                setSurface(Surface(binding.textureView.surfaceTexture))
                isLooping = true
                setVolume(0f, 0f)
                setOnPreparedListener {
                    it.start()
                    applyZoom()
                }
                prepareAsync()
            }
        }

        binding.btnStarted.setOnClickListener {
            NavigateNextPage(this)
        }
    }
    private fun NavigateNextPage(context: Context) {
        val intent = Intent(context, ActivitySelectgender::class.java)
        context.startActivity(intent)
    }

    private fun applyZoom() {
        binding.textureView.let {
            val matrix = Matrix()
            val scaleX = it.width.toFloat() / mediaPlayer!!.videoWidth.toFloat()
            val scaleY = it.height.toFloat() / mediaPlayer!!.videoHeight.toFloat()
            val pivotX = it.width / 2f
            val pivotY = it.height / 2f
            matrix.setScale(scaleX * 1.5f, scaleY * 1.5f, pivotX, pivotY)
            it.setTransform(matrix)
        }
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer?.start()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}