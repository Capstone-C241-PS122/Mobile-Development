package com.grassterra.fitassist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.grassterra.fitassist.databinding.ActivityMainMenuBinding
import com.grassterra.fitassist.databinding.AlertDialogBinding

class MainMenu : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnupload.setOnClickListener {
            showAlert(this, "Choose an Option", {
                // Handle Gallery action
            }, {
                // Handle Camera action
            })
        }
    }
    private fun showAlert(
        context: Context,
        title: String,
        galleryCallback: () -> Unit,
        cameraCallback: () -> Unit
    ) {
        val binding = AlertDialogBinding.inflate(LayoutInflater.from(context))

        binding.alertTitle.text = title

        val alertDialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        binding.galleryButton.setOnClickListener {
            galleryCallback.invoke()
            alertDialog.dismiss()
        }

        binding.cameraButton.setOnClickListener {
            cameraCallback.invoke()
            alertDialog.dismiss()
        }

        binding.cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

}