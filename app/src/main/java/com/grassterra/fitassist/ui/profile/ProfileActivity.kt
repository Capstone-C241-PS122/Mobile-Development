package com.grassterra.fitassist.ui.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.grassterra.fitassist.R
import com.grassterra.fitassist.database.user.Userdata
import com.grassterra.fitassist.databinding.ActivityBmrBinding
import com.grassterra.fitassist.databinding.ActivityProfileBinding
import com.grassterra.fitassist.helper.ViewModelFactory
import com.grassterra.fitassist.ui.mainMenu.MainMenu
import com.grassterra.fitassist.ui.myBody.BMIViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var adapter: ArrayAdapter<CharSequence>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize adapter with custom layouts
        val genders = resources.getStringArray(R.array.gender_array).toList()
        val adapter = object : ArrayAdapter<String>(this, R.layout.spinner_selected_item, genders) {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as TextView).setTextColor(ContextCompat.getColor(context, R.color.black))
                return view
            }
        }
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.etGender.adapter = adapter

        val bmiViewModel = obtainViewModel(this@ProfileActivity)
        setFields(bmiViewModel)

        binding.btnCalculate.setOnClickListener {
            saveData(bmiViewModel)
        }

        binding.btnBack.setOnClickListener {
            goToBack(this)
        }
    }


    private fun saveData(bmiViewModel: BMIViewModel) {
        val weight = binding.etWeight.text.toString().toIntOrNull() ?: 60
        val height = binding.etHeight.text.toString().toIntOrNull() ?: 160
        val age = binding.etAge.text.toString().toIntOrNull() ?: 17
        val gender = binding.etGender.selectedItem.toString()

        val mgender = gender.equals("pria", ignoreCase = true)
        val userData = Userdata(weight, height, age, mgender)

        lifecycleScope.launch(Dispatchers.IO) {
            bmiViewModel.overwriteUser(userData)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@ProfileActivity, "Saved Successfully!", Toast.LENGTH_SHORT).show()

                // Delay the transition to the next activity using Handler
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this@ProfileActivity, MainMenu::class.java)
                    startActivity(intent)
                    finish()
                }, 1000)
            }
        }
    }



    private fun setFields(bmiViewModel: BMIViewModel) {
        lifecycleScope.launch(Dispatchers.IO) {
            val user = bmiViewModel.getUser()
            val height = user?.height.toString()
            val weight = user?.weight.toString()
            val age = user?.age.toString()
            val gender = if (user?.gender == true) "pria" else "wanita"

            withContext(Dispatchers.Main) {
                binding.etHeight.text = Editable.Factory.getInstance().newEditable(height)
                binding.etWeight.text = Editable.Factory.getInstance().newEditable(weight)
                binding.etAge.text = Editable.Factory.getInstance().newEditable(age)
                if (::adapter.isInitialized) {
                    val position = adapter.getPosition(gender)
                    binding.etGender.setSelection(position)
                }
            }
        }
    }

    private fun goToBack(context: Context) {
        val intent = Intent(context, MainMenu::class.java)
        context.startActivity(intent)
        finish()
    }

    private fun obtainViewModel(activity: AppCompatActivity): BMIViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(BMIViewModel::class.java)
    }
}