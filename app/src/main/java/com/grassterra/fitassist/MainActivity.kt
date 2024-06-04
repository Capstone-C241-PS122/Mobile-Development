package com.grassterra.fitassist

import android.content.Context
import android.content.Intent
import android.graphics.Matrix
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import androidx.core.view.isVisible
import com.grassterra.fitassist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.video_background)
        binding.textureView.post {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(this@MainActivity, videoUri)
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
        val intent = Intent(context,ActivitySelectgender::class.java)
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