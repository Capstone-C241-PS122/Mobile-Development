package com.grassterra.fitassist.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.grassterra.fitassist.HomeFragment
import com.grassterra.fitassist.MyBodyFragment
import com.grassterra.fitassist.R
import com.grassterra.fitassist.databinding.ActivityMainMenuBinding
import com.grassterra.fitassist.databinding.AlertDialogBinding
import com.grassterra.fitassist.helper.ImageClassifierHelper
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainMenu : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private val CAPTURE_IMAGE_REQUEST = 2
    private lateinit var currentPhotoPath: String
    private lateinit var binding: ActivityMainMenuBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnupload.setOnClickListener {
            showAlert(this, {
                //gallery action
                if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    startGallery()
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }, {
                if (checkPermission(Manifest.permission.CAMERA)) {
                    startCamera()
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            })
        }
        setupBottomNavigation()
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile: File? = null
        try {
            photoFile = createImageFile()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        if (photoFile != null) {
            val photoURI = FileProvider.getUriForFile(this, "com.grassterra.fitassist.fileprovider", photoFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            val result = selectedImageUri?.let { analyzeImage(it) }
            if (selectedImageUri != null) {
                if (result != null) {
                    Log.d("MainMenu", result.toString())
                    moveToDetailActivity(selectedImageUri, result)
                }
            }
        } else if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val file = File(currentPhotoPath)
            val result = analyzeImage(Uri.fromFile(file))
            moveToDetailActivity(Uri.fromFile(file), result)
        }
    }

    private fun analyzeImage(imageUri: Uri): String {
        var res = ""
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    Log.d("MainMenu", "classifier error!")
                }

                override fun onResults(results: TensorBuffer) {
                    val outputArray = results.floatArray
//                    res = outputArray.joinToString { it.toString() }

                    //Utilize predefined list of labels
                     val classLabels = arrayOf("Lat_pulldown_machine", "Shoulder_press", "elliptical_trainer", "leg_curl_machine", "legpress_machine", "rowing_machine") // define your class labels
                     val resultMap = outputArray.indices.associate { classLabels[it] to outputArray[it] }
                     res = resultMap.entries.joinToString { "${it.key}: ${it.value}" }
                }
            }
        )
        imageClassifierHelper.classifyStaticImage(imageUri)
        return res
    }


    private fun moveToDetailActivity(imageUri: Uri, res: String){
        val intent = Intent(this, DetailExerciseActivity::class.java)
        intent.putExtra("imageUri", imageUri.toString())
        intent.putExtra("result", res)
        startActivity(intent)
    }

    private fun showAlert(
        context: Context,
        galleryCallback: () -> Unit,
        cameraCallback: () -> Unit
    ) {
        val binding = AlertDialogBinding.inflate(LayoutInflater.from(context))
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

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    private fun setupBottomNavigation() {
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.id_home -> selectedFragment = HomeFragment()
                R.id.id_mybody -> selectedFragment = MyBodyFragment()
            }
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.idHostFragment, selectedFragment).commit()
            }
            true
        }
        bottomNavigationView.selectedItemId = R.id.id_home
    }
}