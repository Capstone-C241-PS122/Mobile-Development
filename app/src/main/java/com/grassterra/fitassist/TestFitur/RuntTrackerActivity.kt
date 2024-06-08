package com.grassterra.fitassist.TestFitur

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.grassterra.fitassist.databinding.ActivityRuntTrackerBinding
import kotlin.math.roundToInt

class RuntTrackerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRuntTrackerBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var lastLocation: Location? = null
    private var totalDistance = 0.0
    private var caloriesBurned = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRuntTrackerBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    if (lastLocation != null) {
                        val distance = lastLocation!!.distanceTo(location) / 1000.0
                        totalDistance += distance
                        caloriesBurned += calculateCalories(distance, 70.0)
                    }
                    lastLocation = location
                }
                updateUI(totalDistance, caloriesBurned)
            }
        }
        startLocationUpdates()
    }
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun calculateCalories(distance: Double, weight: Double): Double {
        val met = 8.0
        val time = distance / 10.0
        return met * weight * time
    }

    private fun updateUI(distance: Double, calories: Double) {
        binding.tvDistance.text = "Distance: ${distance.roundToInt()} km"
        binding.tvCalories.text = "Calories: ${calories.roundToInt()} kcal"
    }


    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

}