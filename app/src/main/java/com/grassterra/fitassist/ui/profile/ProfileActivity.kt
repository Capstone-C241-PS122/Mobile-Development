package com.grassterra.fitassist.ui.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.Toast
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
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ArrayList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.etGender.adapter = adapter
        adapter.addAll(resources.getStringArray(R.array.gender_array).toList())
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
        lifecycleScope.launch(Dispatchers.IO) {
            val weight = binding.etWeight.text.toString().toIntOrNull() ?: 60
            val height = binding.etHeight.text.toString().toIntOrNull() ?: 160
            val age = binding.etAge.text.toString().toIntOrNull() ?: 17
            val gender = binding.etGender.selectedItem.toString()

            val mgender = gender.equals("pria", ignoreCase = true)
            val userData = Userdata(weight, height, age, mgender)
            bmiViewModel.overwriteUser(userData)

            withContext(Dispatchers.Main){
                Toast.makeText(this@ProfileActivity, "Saved Succesfully!", Toast.LENGTH_SHORT).show()
                delay(1000)
                val intent = Intent(this@ProfileActivity, MainMenu::class.java)
                startActivity(intent)
                finish()
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