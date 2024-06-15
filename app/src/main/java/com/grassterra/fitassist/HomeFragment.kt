package com.grassterra.fitassist
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.grassterra.fitassist.databinding.FragmentHomeBinding

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
