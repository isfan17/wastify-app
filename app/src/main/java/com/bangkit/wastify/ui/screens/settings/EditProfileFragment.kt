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
import com.bangkit.wastify.databinding.FragmentEditProfileBinding
import com.bangkit.wastify.ui.viewmodels.AuthViewModel
import com.bangkit.wastify.utils.Helper.isValidEmail
import com.bangkit.wastify.utils.Helper.toast
import com.bangkit.wastify.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserData()

        // Validating update profile result
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.profileUpdatesFlow.collect { state ->
                    if (state != null) {
                        when (state) {
                            UiState.Loading -> showLoading(true)
                            is UiState.Failure -> {
                                showLoading(false)
                                toast(state.error.toString())
                            }
                            is UiState.Success -> {
                                showLoading(false)
                                toast(getString(R.string.msg_profile_updated))
                                findNavController().navigateUp()
                            }
                        }
                    }
                }
            }
        }

        binding.btnUpdate.setOnClickListener {
            if (dataChanged()) {
                updateProfile()
            } else {
                toast(getString(R.string.msg_no_data_changed))
            }
        }

        binding.btnForgot.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_forgotPasswordFragment)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUserData() {
        binding.edtFullName.setText(authViewModel.currentUser?.displayName.toString())
        binding.edtEmail.setText(authViewModel.currentUser?.email.toString())
    }

    private fun dataChanged(): Boolean {
        val nameEntry = binding.edtFullName.text.toString()
        val emailEntry = binding.edtEmail.text.toString()
        return !(nameEntry == authViewModel.currentUser?.displayName.toString()
                && emailEntry == authViewModel.currentUser?.email.toString())
    }

    private fun updateProfile() {
        val nameEntry = binding.edtFullName.text.toString()
        val emailEntry = binding.edtEmail.text.toString()

        // Checks form requirement
        when {
            nameEntry.isEmpty() -> binding.edtFullName.error = getString(R.string.msg_field_required)
            emailEntry.isEmpty() -> binding.edtEmail.error = getString(R.string.msg_field_required)
            !isValidEmail(emailEntry) -> binding.edtEmail.error = getString(R.string.msg_input_valid_email)

            else -> {
                authViewModel.updateProfile(nameEntry, emailEntry)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}