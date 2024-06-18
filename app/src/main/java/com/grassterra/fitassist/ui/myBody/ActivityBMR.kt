package com.grassterra.fitassist.ui.myBody

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.grassterra.fitassist.R
import com.grassterra.fitassist.databinding.ActivityBmrBinding
import com.grassterra.fitassist.helper.ViewModelFactory
import com.grassterra.fitassist.ui.mainMenu.MainMenu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityBMR : AppCompatActivity() {
    private lateinit var binding: ActivityBmrBinding
    private lateinit var adapter: ArrayAdapter<CharSequence>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmrBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        val bmiViewModel = obtainViewModel(this@ActivityBMR)
        setFields(bmiViewModel)
        binding.btnCalculate.setOnClickListener {
            calculateBMR()
        }
        binding.btnReset.setOnClickListener{
            resetFields()
        }
        binding.btnBack.setOnClickListener {
            goToBack(this)
        }
    }
    private fun resetFields() {
        binding.etHeight.text.clear()
        binding.etWeight.text.clear()
        binding.resultTextView.text = ""
    }

    private fun calculateBMR() {
        val weight = binding.etWeight.text.toString().toDoubleOrNull() ?: 0.0
        val height = binding.etHeight.text.toString().toDoubleOrNull() ?: 0.0
        val age = binding.etAge.text.toString().toIntOrNull() ?: 0
        val gender = binding.etGender.selectedItem.toString()

        val bmr = if (gender.equals("pria", ignoreCase = true)) {
            88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)
        } else {
            447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)
        }
        val roundBmr = bmr.toInt()

        binding.resultTextView.text = "Result BMR: $roundBmr Calories Per Day"
    }

    private fun setFields(bmiViewModel: BMIViewModel) {
        lifecycleScope.launch(Dispatchers.IO) {
            val user = bmiViewModel.getUser()
            val height = user?.height.toString()
            val weight = user?.weight.toString()
            val age = user?.age.toString()
            val gender = if (user?.gender == true) 0 else 1

            withContext(Dispatchers.Main) {
                binding.etHeight.text = Editable.Factory.getInstance().newEditable(height)
                binding.etWeight.text = Editable.Factory.getInstance().newEditable(weight)
                binding.etAge.text = Editable.Factory.getInstance().newEditable(age)
                binding.etGender.setSelection(gender)
            }
        }
    }

    private fun goToBack(context: Context) {
        val intent = Intent(context, MainMenu::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(MainMenu.TARGET_FRAGMENT, MainMenu.FRAGMENT_TWO)
        }
        context.startActivity(intent)
        finish()
    }

    private fun obtainViewModel(activity: AppCompatActivity): BMIViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(BMIViewModel::class.java)
    }
}