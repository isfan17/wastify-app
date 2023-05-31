package com.bangkit.wastify.ui.screens.splash

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bangkit.wastify.R
import com.bangkit.wastify.databinding.FragmentSplashBinding
import com.bangkit.wastify.ui.viewmodels.AuthViewModel
import com.bangkit.wastify.utils.Constants.KEY_FIRST_TIME_TOGGLE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    @Inject
    lateinit var sharedPref: SharedPreferences

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)

            // Checking if user opens the app for the first time
            if (isFirstAppOpen()) {
                findNavController().navigate(R.id.action_splashFragment_to_onBoardingContainerFragment)
            } else {
                // Checking if the user already login
                if (authViewModel.currentUser != null) {
                    findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                } else {
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }
            }
        }
    }

    private fun isFirstAppOpen(): Boolean {
        return sharedPref.getBoolean(KEY_FIRST_TIME_TOGGLE, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}