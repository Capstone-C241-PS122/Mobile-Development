package com.grassterra.fitassist
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.grassterra.fitassist.databinding.FragmentHomeBinding
import com.grassterra.fitassist.ui.MainMenu
import java.io.File
import java.io.IOException

class HomeFragment : Fragment() {
    private val CAPTURE_IMAGE_REQUEST = 2
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = binding.searchView
        context?.let { ctx ->
            searchView.background = ContextCompat.getDrawable(ctx, R.drawable.background_searchview)
        }
        val searchIcon: ImageView? = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon)
        val closeIcon: ImageView? = searchView.findViewById(androidx.appcompat.R.id.search_close_btn)

//        binding.btnScan.setOnClickListener {
//            if (checkPermission(Manifest.permission.CAMERA)) {
//                startCamera()
//            } else {
//                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
//            }
//        }
        searchIcon?.setOnClickListener {
            searchView.setQuery("", false)
            searchView.clearFocus()
            searchIcon.setImageResource(R.drawable.search_24px)
        }
        closeIcon?.setOnClickListener {
            searchView.setQuery("", false)
            searchView.clearFocus()
            searchIcon?.setImageResource(R.drawable.search_24px)
        }
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                searchIcon?.setImageResource(R.drawable.close_24px)
            } else {
                searchIcon?.setImageResource(R.drawable.search_24px)
            }
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(context, "Search for: $query", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun startCamera() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        var photoFile: File? = null
//        try {
//            photoFile = createImageFile()
//        } catch (ex: IOException) {
//            ex.printStackTrace()
//        }
//        if (photoFile != null) {
//            val photoURI = FileProvider.getUriForFile(this, "com.grassterra.fitassist.fileprovider", photoFile)
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST)
//        }
//    }
//
//    private fun checkPermission(permission: String): Boolean {
//        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
//    }
//
//    private val requestPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted: Boolean ->
//        if (!isGranted) {
//            Log.d("req", "denied")
//        }
//    }


}
