package com.grassterra.fitassist.ui.mainMenu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.grassterra.fitassist.R
import com.grassterra.fitassist.ui.myBody.ActivityBMI
import com.grassterra.fitassist.ui.myBody.ActivityBMR
import com.grassterra.fitassist.ui.myBody.ActivityCalculateNutrition
import com.grassterra.fitassist.ui.myBody.ActivityHydration

/**
 * A simple [Fragment] subclass.
 * Use the [MyBodyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyBodyFragment : Fragment() {
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_body, container, false)
        view.findViewById<ImageButton>(R.id.btnNavigateBMI)?.setOnClickListener {
            val intent = Intent(requireContext(), ActivityBMI::class.java)
            startActivity(intent)
        }
        view.findViewById<ImageButton>(R.id.btnNavigateBMR)?.setOnClickListener{
            val intent = Intent(requireContext(), ActivityBMR::class.java)
            startActivity(intent)
        }
        view.findViewById<ImageButton>(R.id.btnNavigateHydration)?.setOnClickListener{
            val intent = Intent(requireContext(), ActivityHydration::class.java)
            startActivity(intent)
        }
        view.findViewById<ImageButton>(R.id.btnNavigateNutritionCalculate)?.setOnClickListener{
            val intent = Intent(requireContext(), ActivityCalculateNutrition::class.java)
            startActivity(intent)
        }

        return view
    }
}