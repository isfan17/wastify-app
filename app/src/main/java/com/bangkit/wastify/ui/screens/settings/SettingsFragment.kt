package com.bangkit.wastify.ui.screens.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bangkit.wastify.R
import com.bangkit.wastify.databinding.FragmentSettingsBinding
import com.bangkit.wastify.ui.viewmodels.AuthViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

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

        // Setting user full name and email
        updateUserData()

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

        // Back btn
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun updateUserData() {
        binding.tvFullName.text = authViewModel.currentUser?.displayName
        binding.tvEmail.text = authViewModel.currentUser?.email
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}