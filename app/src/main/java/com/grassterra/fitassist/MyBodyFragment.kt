package com.grassterra.fitassist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.grassterra.fitassist.ui.ActivityBMI
import com.grassterra.fitassist.ui.ActivityBMR
import com.grassterra.fitassist.ui.ActivityCalculateNutrition
import com.grassterra.fitassist.ui.ActivityHydration

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyBodyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyBodyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyBodyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                MyBodyFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}