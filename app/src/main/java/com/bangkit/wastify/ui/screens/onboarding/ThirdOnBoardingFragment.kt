package com.bangkit.wastify.ui.screens.onboarding

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bangkit.wastify.R
import com.bangkit.wastify.databinding.FragmentThirdOnBoardingBinding
import com.bangkit.wastify.utils.Constants.KEY_FIRST_TIME_TOGGLE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ThirdOnBoardingFragment : Fragment() {

    private var _binding: FragmentThirdOnBoardingBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGetStarted.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingContainerFragment_to_loginFragment)
            onBoardingFinished()
        }
    }

    private fun onBoardingFinished() {
        sharedPref.edit()
            .putBoolean(KEY_FIRST_TIME_TOGGLE, false)
            .apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}