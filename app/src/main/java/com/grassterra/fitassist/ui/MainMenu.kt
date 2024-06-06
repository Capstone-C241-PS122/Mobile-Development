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
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.grassterra.fitassist.HomeFragment
import com.grassterra.fitassist.MyBodyFragment
import com.grassterra.fitassist.R
import com.grassterra.fitassist.database.user.Userdata
import com.grassterra.fitassist.databinding.ActivityMainMenuBinding
import com.grassterra.fitassist.databinding.AlertDialogBinding
import com.grassterra.fitassist.helper.ImageClassifierHelper
import com.grassterra.fitassist.helper.ParcelableMap
import com.grassterra.fitassist.helper.ViewModelFactory
import com.grassterra.fitassist.helper.compressImageFile
import com.grassterra.fitassist.helper.uriToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class MainMenu : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private val CAPTURE_IMAGE_REQUEST = 2
    private lateinit var currentPhotoPath: String
    private lateinit var binding: ActivityMainMenuBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private lateinit var userData: Userdata

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainViewModel = obtainViewModel(this@MainMenu)
        val flag = intent.getBooleanExtra("flag", false)

        Log.d("MainMenu", flag.toString())

        if (flag) {
            userData = intent.getParcelableExtra("userdata") ?: Userdata()
            lifecycleScope.launch(Dispatchers.IO) {
                if (mainViewModel.isEmpty()) {
                    mainViewModel.insertUser(userData)
                    Log.d("MainMenu", "insert ${userData.weight}")
                } else {
                    mainViewModel.overwriteUser(userData)
                    Log.d("MainMenu", "overwrite into ${userData.weight}")
                }
                withContext(Dispatchers.Main) {
                    Log.d("MainMenu", "age: ${userData.age}, height: ${userData.height}, gender: ${userData.gender}")
                }
            }
        } else {
            //TODO: MOVE TO MAINACTIVITY
            userData = Userdata()
        }

        binding.btnupload.setOnClickListener {
            showAlert(this, {
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
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navigationView: NavigationView = findViewById(R.id.navigationView)
        binding.btnSidebar.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_history -> {
                    val intent = Intent(this@MainMenu, HistoryActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_library_video -> {
                    val intent = Intent(this@MainMenu,LibraryVideo::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_feedback -> {
                    val intent = Intent(this@MainMenu,FeedbackActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
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
            val compressedFile =
                selectedImageUri?.let { uriToFile(this, it)?.let { compressImageFile(it, 2) } }
            val result = analyzeImage(Uri.fromFile(compressedFile))
            if (selectedImageUri != null) {
                Log.d("MainMenu", result.toString())
                moveToDetailActivity(Uri.fromFile(compressedFile), result)
            }
        } else if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val file = File(currentPhotoPath)
            val compressedFile = compressImageFile(file, 2)
            val result = analyzeImage(Uri.fromFile(compressedFile))
            moveToDetailActivity(Uri.fromFile(compressedFile), result)
        }
    }

    private fun analyzeImage(imageUri: Uri): Map<String, Float> {
        var res: Map<String, Float> = emptyMap()
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    Log.d("MainMenu", "classifier error!")
                }

                override fun onResults(results: TensorBuffer) {
                    val outputArray = results.floatArray

                    // Utilize predefined list of labels
                    val classLabels = arrayOf(
                        "cable_machine",
                        "calfraise_machine",
                        "chestfly_machine",
                        "elliptical_trainer",
                        "hacksquat_machine",
                        "hyperextension_bench",
                        "latpulldown_machine",
                        "legcurl_machine",
                        "legpress_machine",
                        "rotarycalf_machine",
                        "rowing_machine",
                        "shoulder_press",
                        "smith_machine"
                    )

                    // Create a map of class labels to their corresponding output values
                    val resultMap = outputArray.indices.associate { classLabels[it] to outputArray[it] }

                    // Sort the map by values in descending order
                    res = resultMap.entries.sortedByDescending { it.value }.associate { it.toPair() }
                }
            }
        )
        imageClassifierHelper.classifyStaticImage(imageUri)
        return res
    }


    private fun moveToDetailActivity(imageUri: Uri, map: Map<String, Float>){
        val intent = Intent(this, DetailExerciseActivity::class.java)
        val res = ParcelableMap(map)
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

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(MainViewModel::class.java)
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