package com.grassterra.fitassist.ui

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
    private var updateStopwatch = object : Runnable {
        override fun run(){
            if(isRunningNow){
                elapsedTime += 1000
                val seconds = (elapsedTime / 1000) % 60
                val minutes = (elapsedTime / (1000 * 60)) % 60
                binding.tvStopwatch.text = String.format("%02d:%02d", minutes, seconds)
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
        binding.numberPickerWeight.apply {
            minValue = 0
            maxValue = 40
            value = 5
            setOnValueChangedListener { _, _, newVal ->
                Log.d("InputWeight", newVal.toString())
            }
        }
        binding.btnReset.setOnClickListener{
            resetStopwatch()
        }
        binding.btnStopwatch.setOnClickListener {
            if (isRunningNow) {
                stopStopwatch()
            } else {
                startOrResumeStopwatch()
            }
        }
        savedInstanceState?.let {
            isRunningNow = it.getBoolean("isRunningNow", false)
            elapsedTime = it.getLong("elapsedTime", 0L)
            if (isRunningNow) {
                startOrResumeStopwatch()
            } else {
                updateStopwatchDisplay()
            }
        }
    }

    private fun updateStopwatchDisplay() {
        val seconds = (elapsedTime / 1000) % 60
        val minutes = (elapsedTime / (1000 * 60)) % 6
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
            handler.post(updateStopwatch)
            binding.btnStopwatch.text = "Stop"
        }
    }
    private fun stopStopwatch() {
        if (isRunningNow) {
            isRunningNow = false
            handler.removeCallbacks(updateStopwatch)
            binding.btnStopwatch.text = "Resume"
        }
    }
    private fun resetStopwatch() {
        elapsedTime = 0L
        isRunningNow = false
        binding.btnStopwatch.text = "Start"
        updateUI()
    }

    private fun updateUI() {
        binding.tvStopwatch.text = "00:00"
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isRunningNow", isRunningNow)
        outState.putLong("elapsedTime", elapsedTime)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handler.removeCallbacks(updateStopwatch)
    }
    override fun onPause() {
        super.onPause()
        if (isRunningNow) {
            elapsedTime = System.currentTimeMillis() - startTime
        }
    }
}
