package com.grassterra.fitassist.ui.userInput

import android.content.Context
import android.content.Intent
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.Surface
import android.view.TextureView
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

        binding.textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(this@MainActivity, Uri.parse(videoUrl))
                    setSurface(Surface(surface))
                    isLooping = true
                    setVolume(0f, 0f)
                    setOnPreparedListener {
                        it.start()
                        applyZoom()
                    }
                    prepareAsync()
                }
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}
            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                mediaPlayer?.release()
                mediaPlayer = null
                return true
            }
            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
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
