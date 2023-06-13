package com.bangkit.wastify.ui.screens.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bangkit.wastify.R
import com.bangkit.wastify.data.model.User
import com.bangkit.wastify.databinding.FragmentSettingsBinding
import com.bangkit.wastify.ui.viewmodels.AuthViewModel
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set user data
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.userFlow.collectLatest { setUserData(it) }
            }
        }

        // Do logout process
        binding.btnLogout.setOnClickListener { _ ->
            context?.let { it ->
                MaterialAlertDialogBuilder(it)
                    .setTitle(getString(R.string.logout))
                    .setMessage(getString(R.string.msg_logout))
                    .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        authViewModel.logout()
                        findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
                    }
                    .show()
            }
        }

        // Move to edit profile page
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_editProfileFragment)
        }

        // Back btn
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun setUserData(user: User?) {
        binding.tvFullName.text = user?.name
        binding.tvEmail.text = user?.email

        if (user?.imageUrl == null) {
            binding.ivProfile.setImageResource(R.drawable.default_profile)
        } else {
            Glide.with(this)
                .load(user.imageUrl)
                .into(binding.ivProfile)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}