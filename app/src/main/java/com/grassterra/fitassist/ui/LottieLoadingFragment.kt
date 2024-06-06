package com.grassterra.fitassist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.grassterra.fitassist.databinding.FragmentLottieLoadingBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LottieLoadingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LottieLoadingFragment : Fragment() {

    private var _binding: FragmentLottieLoadingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLottieLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun startAnimation() {
        binding.lottieAnimationViewLoading.playAnimation()
    }

    fun stopAnimation() {
        binding.lottieAnimationViewLoading.cancelAnimation()
    }
}