package com.grassterra.fitassist.ui.mainMenu

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.grassterra.fitassist.databinding.FragmentBurnCaloriesTrackerBinding

class BurnCaloriesTrackerFragment : Fragment() {
    private var _binding: FragmentBurnCaloriesTrackerBinding? = null
    private val binding get() = _binding!!
    private var isRunningNow = false
    private var elapsedTime = 0L
    private var startTime = 0L
    private val handler = Handler(Looper.getMainLooper())

    private val updateStopwatchRunnable = object : Runnable {
        override fun run() {
            if (isRunningNow) {
                elapsedTime = System.currentTimeMillis() - startTime
                updateStopwatchDisplay()
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBurnCaloriesTrackerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNumberPicker()
        setupButtons()

        savedInstanceState?.let {
            isRunningNow = it.getBoolean("isRunningNow", false)
            elapsedTime = it.getLong("elapsedTime", 0L)
            if (isRunningNow) {
                startOrResumeStopwatch()
            } else {
                updateStopwatchDisplay()
            }
        }
        calculateAndDisplayCalories()
    }

    private fun setupNumberPicker() {
        binding.numberPickerWeight.apply {
            minValue = 0
            maxValue = 40
            value = 5
            setOnValueChangedListener { _, _, _ ->
                calculateAndDisplayCalories()
            }
        }
    }

    private fun setupButtons() {
        binding.btnReset.setOnClickListener {
            resetStopwatch()
        }

        binding.btnStopwatch.setOnClickListener {
            if (isRunningNow) {
                stopStopwatch()
            } else {
                startOrResumeStopwatch()
            }
        }
    }
    private fun calculateAndDisplayCalories() {
        calculateAndDisplayCaloriesWithElapsedTime(elapsedTime)
    }

    private fun calculateAndDisplayCaloriesWithElapsedTime(elapsedTime: Long) {
        val beratBadan = 60.0
        val beratBeban = binding.numberPickerWeight.value.toDouble()
        val durasiWaktu = getElapsedTimeInMinutes(elapsedTime)

        val metValues = mapOf(
            1.0 to 2.3,
            2.0 to 3.0,
            3.0 to 3.5,
            4.0 to 4.0,
            5.0 to 5.0,
            6.0 to 5.5,
            7.0 to 6.0,
            8.0 to 6.0,
            9.0 to 6.5,
            10.0 to 6.5,
            12.0 to 7.0,
            15.0 to 7.5,
            18.0 to 8.0,
            20.0 to 8.0,
            25.0 to 8.5,
            30.0 to 9.0,
            35.0 to 9.5,
            40.0 to 10.0
        )

        val metValue = metValues[beratBeban] ?: 1.0
        val caloriesBurnedPerMinute = (metValue * beratBadan * 3.5) / 200
        val totalCaloriesBurned = caloriesBurnedPerMinute * durasiWaktu

        Log.d("CaloriesCalculation", "beratBadan: $beratBadan")
        Log.d("CaloriesCalculation", "beratBeban: $beratBeban")
        Log.d("CaloriesCalculation", "durasiWaktu: $durasiWaktu")
        Log.d("CaloriesCalculation", "metValue: $metValue")

        binding.totalBurnCalories.text = "Total Burn Calories : %.2f kalori".format(totalCaloriesBurned)
    }

    private fun resetStopwatch() {
        val elapsedTimeBeforeReset = elapsedTime
        elapsedTime = 0L
        isRunningNow = false
        binding.btnStopwatch.text = "Start"
        updateStopwatchDisplay()
        calculateAndDisplayCaloriesWithElapsedTime(elapsedTimeBeforeReset)
        binding.totalBurnCalories.visibility = View.VISIBLE
    }

    private fun getElapsedTimeInMinutes(elapsedTime: Long): Double {
        Log.d("ElapsedTime", "elapsedTime: $elapsedTime")
        return elapsedTime / 1000.0 / 60.0
    }
    private fun updateStopwatchDisplay() {
        val seconds = (elapsedTime / 1000) % 60
        val minutes = (elapsedTime / (1000 * 60)) % 60
        binding.tvStopwatch.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun startOrResumeStopwatch() {
        if (!isRunningNow) {
            if (elapsedTime == 0L) {
                startTime = System.currentTimeMillis()
            } else {
                startTime = System.currentTimeMillis() - elapsedTime
            }
            isRunningNow = true
            handler.post(updateStopwatchRunnable)
            binding.btnStopwatch.text = "Stop"
        }
    }

    private fun stopStopwatch() {
        if (isRunningNow) {
            isRunningNow = false
            handler.removeCallbacks(updateStopwatchRunnable)
            binding.btnStopwatch.text = "Resume"
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isRunningNow", isRunningNow)
        outState.putLong("elapsedTime", elapsedTime)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handler.removeCallbacks(updateStopwatchRunnable)
    }

    override fun onPause() {
        super.onPause()
        if (isRunningNow) {
            elapsedTime = System.currentTimeMillis() - startTime
        }
    }
}

