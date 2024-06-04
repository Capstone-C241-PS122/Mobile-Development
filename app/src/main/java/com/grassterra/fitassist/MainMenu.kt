package com.grassterra.fitassist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.grassterra.fitassist.databinding.ActivityMainMenuBinding
import com.grassterra.fitassist.databinding.AlertDialogBinding

class MainMenu : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()
        binding.btnupload.setOnClickListener {
            showAlert(this, {
                // Handle Gallery action
            }, {
                // Handle Camera action
            })
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

}