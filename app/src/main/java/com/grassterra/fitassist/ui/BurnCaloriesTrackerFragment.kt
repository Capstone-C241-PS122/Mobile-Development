package com.grassterra.fitassist.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.grassterra.fitassist.databinding.FragmentBurnCaloriesTrackerBinding
import kotlin.math.roundToInt

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BurnCaloriesTrackerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BurnCaloriesTrackerFragment : Fragment() {
    private lateinit var binding: FragmentBurnCaloriesTrackerBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var lastLocation: Location? = null
    private var totalDistance = 0.0
    private var caloriesBurned = 0.0
    private var isStopwatchRunning = false
    private var stopwatchStartTime = 0L
    private var elapsedTime = 0L
    private val stopwatchHandler = Handler(Looper.getMainLooper())
    private val stopwatchRunnable = object : Runnable {
        override fun run() {
            if (isStopwatchRunning) {
                val now = SystemClock.elapsedRealtime()
                elapsedTime = now - stopwatchStartTime
                updateStopwatchUI()
                stopwatchHandler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBurnCaloriesTrackerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                if (isStopwatchRunning) {
                    for (location in locationResult.locations) {
                        if (lastLocation != null) {
                            val distance = lastLocation!!.distanceTo(location) / 1000.0
                            if (distance > 0.01){
                                val elapsedTime = (location.time - lastLocation!!.time) / 1000.0
                                totalDistance += distance
                                caloriesBurned += calculateCalories(distance,elapsedTime, 65.0)
                            }
                        }
                        lastLocation = location
                    }
                    updateUI(totalDistance, caloriesBurned)
                }
            }
        }

        val buttonClickActions = listOf<Pair<View, () -> Unit>>(
            Pair(binding.btnStartStopwatch, ::startStopwatch),
            Pair(binding.btnStopStopwatch, ::stopStopwatch),
            Pair(binding.btnResetStopwatch, ::resetStopwatch)
        )
        buttonClickActions.forEach { (button, action) ->
            button.setOnClickListener { action() }
        }
        isStopwatchRunning = false
        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun calculateCalories(distance: Double, elapsedTime: Double, weight: Double): Double {
        val speed = if (elapsedTime > 0) distance / (elapsedTime / 3600) else 0.0
        val met = when {
            speed <= 8 -> 8.3
            speed <= 9.7 -> 9.8
            speed <= 11.3 -> 11.0
            speed <= 12.9 -> 11.8
            speed <= 14.5 -> 12.8
            else -> 14.5
        }
        val time = if (speed > 0) distance / speed else 0.0
        return met * weight * time
    }

    private fun updateUI(distance: Double, calories: Double) {
        binding.tvDistance.text = " ${distance.roundToInt()} km"
        binding.tvCalories.text = " ${calories.roundToInt()} kcal"
    }

    private fun updateStopwatchUI() {
        val hours = (elapsedTime / 3600000).toInt()
        val minutes = ((elapsedTime % 3600000) / 60000).toInt()
        val seconds = ((elapsedTime % 60000) / 1000).toInt()
        binding.tvStopwatch.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun startStopwatch() {
        if (!isStopwatchRunning) {
            stopwatchStartTime = SystemClock.elapsedRealtime()
            stopwatchHandler.post(stopwatchRunnable)
            isStopwatchRunning = true
            binding.btnStartStopwatch.visibility = View.INVISIBLE
            binding.lottieAnimationView.visibility = View.INVISIBLE
            binding.shadowBtn.visibility = View.INVISIBLE
            binding.btnStopStopwatch.isEnabled = true
            binding.btnResetStopwatch.isEnabled = true
        }
    }

    private fun stopStopwatch() {
        if (isStopwatchRunning) {
            elapsedTime += SystemClock.elapsedRealtime() - stopwatchStartTime
            isStopwatchRunning = false
            stopwatchHandler.removeCallbacks(stopwatchRunnable)
            binding.btnStartStopwatch.visibility = View.VISIBLE
            binding.lottieAnimationView.visibility = View.VISIBLE
            binding.shadowBtn.visibility = View.VISIBLE
            binding.btnStopStopwatch.isEnabled = false
        }
    }

    private fun resetStopwatch() {
        if (!isStopwatchRunning) {
            elapsedTime = 0L
            updateStopwatchUI()
            binding.btnStartStopwatch.visibility = View.VISIBLE
            binding.lottieAnimationView.visibility = View.VISIBLE
            binding.shadowBtn.visibility = View.VISIBLE
            binding.btnStartStopwatch.visibility = View.VISIBLE
            binding.btnResetStopwatch.isEnabled = false
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        if (isStopwatchRunning) {
            stopwatchHandler.removeCallbacks(stopwatchRunnable)
        }
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
        if (isStopwatchRunning) {
            stopwatchHandler.post(stopwatchRunnable)
        }
    }
}
