package com.grassterra.fitassist.ui.myBody

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.grassterra.fitassist.databinding.ActivityHydrationBinding
import com.grassterra.fitassist.helper.ViewModelFactory
import com.grassterra.fitassist.ui.mainMenu.MainMenu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityHydration : AppCompatActivity() {
    private lateinit var binding: ActivityHydrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHydrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bmiViewModel = obtainViewModel(this@ActivityHydration)
        setFields(bmiViewModel)
        binding.btnReset.setOnClickListener{
            resetFields()
        }
        binding.btnCalculate.setOnClickListener{
            resultHydration()
        }
        binding.btnBack.setOnClickListener {
            goToBack(this)
        }
    }
    private fun resetFields() {
        binding.etWeight.text.clear()
        binding.resultTextView.text = ""
    }
    private fun resultHydration(){
        val weight = binding.etWeight.text.toString().toDoubleOrNull() ?: 0.0
        val faktoKonversi = 37
        val hydration = weight * faktoKonversi
        val roundHydration = hydration.toInt()
        binding.resultTextView.text = "$roundHydration ml/day "
    }

    private fun setFields(bmiViewModel: BMIViewModel) {
        lifecycleScope.launch(Dispatchers.IO) {
            val weight = bmiViewModel.getUser()?.weight.toString()
            withContext(Dispatchers.Main) {
                binding.etWeight.text = Editable.Factory.getInstance().newEditable(weight)
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