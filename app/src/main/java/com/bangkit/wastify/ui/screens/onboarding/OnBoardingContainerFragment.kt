package com.bangkit.wastify.ui.screens.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.wastify.databinding.FragmentOnBoardingContainerBinding
import com.bangkit.wastify.ui.adapters.OnBoardingAdapter

class OnBoardingContainerFragment : Fragment() {

    private var _binding: FragmentOnBoardingContainerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingContainerBinding.inflate(inflater, container, false)

        val fragments = arrayListOf<Fragment>(
            FirstOnBoardingFragment(),
            SecondOnBoardingFragment(),
            ThirdOnBoardingFragment()
        )

        val adapter = OnBoardingAdapter(
            fragments,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = adapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}