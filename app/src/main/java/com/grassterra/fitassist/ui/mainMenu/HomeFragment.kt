package com.grassterra.fitassist.ui.mainMenu
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.grassterra.fitassist.databinding.FragmentHomeBinding
import com.grassterra.fitassist.helper.ImageClassifierHelper
import com.grassterra.fitassist.helper.ParcelableMap
import com.grassterra.fitassist.helper.ViewModelFactory
import com.grassterra.fitassist.response.ListArticleItem
import com.grassterra.fitassist.ui.adapter.ArticleAdapter
import com.grassterra.fitassist.ui.exercise.DetailExerciseActivity
import com.grassterra.fitassist.ui.profile.ProfileActivity
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {
    private val CAPTURE_IMAGE_REQUEST = 2
    private lateinit var currentPhotoPath: String
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeViewModel = obtainViewModel(requireActivity())

        homeViewModel.articleList.observe(viewLifecycleOwner){data ->
            setData(data)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }
        binding.btnUser.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            startActivity(intent)
        }
        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY > 500 && binding.topBar.visibility != View.VISIBLE) {
                fadeInView(binding.topBar)
                fadeInView(binding.tvtopBar)
            } else if (scrollY < 400 && binding.topBar.visibility == View.VISIBLE) {
                fadeOutView(binding.topBar)
                fadeOutView(binding.tvtopBar)
            }
        })
        binding.btnScan.setOnClickListener {
            if (checkPermission(Manifest.permission.CAMERA)) {
                startCamera()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setData(userList: List<ListArticleItem>) {
        val adapter = ArticleAdapter()
        val filteredUserList = userList.filter { !it.description.isNullOrEmpty() }
        adapter.submitList(filteredUserList)
        binding.recyclerView.adapter = adapter
    }

    private fun obtainViewModel(activity: FragmentActivity): HomeViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HomeViewModel::class.java)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile: File? = null
        try {
            photoFile = createImageFile()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        if (photoFile != null) {
            val photoURI = FileProvider.getUriForFile(requireActivity(), "com.grassterra.fitassist.fileprovider", photoFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
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
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val file = File(currentPhotoPath)
            val result = analyzeImage(Uri.fromFile(file))
            moveToDetailActivity(Uri.fromFile(file), result)
        }
    }

    private fun analyzeImage(imageUri: Uri): Map<String, Float> {
        var res: Map<String, Float> = emptyMap()
        imageClassifierHelper = ImageClassifierHelper(
            context = requireActivity(),
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    Log.d("MainMenu", "classifier error!")
                }

                override fun onResults(results: TensorBuffer) {
                    val outputArray = results.floatArray

                    // Utilize predefined list of labels
                    val classLabels = arrayOf(
                        "cable_machine",
                        "calf_raise_machine",
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

        val bitmap = loadImageAsBitmap(imageUri)
        bitmap?.let {
            imageClassifierHelper.classifyStaticImage(it)
        }

        return res
    }

    private fun loadImageAsBitmap(imageUri: Uri): Bitmap? {
        return try {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(requireContext().contentResolver, imageUri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
            }

            // Ensure the bitmap is in a mutable format that supports pixel access
            bitmap.copy(Bitmap.Config.ARGB_8888, true)?.let {
                Bitmap.createScaledBitmap(it, 224, 224, true)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun moveToDetailActivity(imageUri: Uri, map: Map<String, Float>){
        val intent = Intent(requireActivity(), DetailExerciseActivity::class.java)
        val res = ParcelableMap(map)
        intent.putExtra("imageUri", imageUri.toString())
        intent.putExtra("result", res)
        startActivity(intent)
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(requireActivity(), permission) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        Log.d("camera", "denied")
    }
}
