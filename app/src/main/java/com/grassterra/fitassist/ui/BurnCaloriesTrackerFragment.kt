package com.grassterra.fitassist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.grassterra.fitassist.databinding.FragmentBurnCaloriesTrackerBinding

class BurnCaloriesTrackerFragment : Fragment() {
    private lateinit var binding: FragmentBurnCaloriesTrackerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBurnCaloriesTrackerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStartStopwatch.setOnClickListener {
            //
        }

        binding.btnStopStopwatch.setOnClickListener {
         //
        }

        binding.btnResetStopwatch.setOnClickListener {
            //
        }

    }


}
