package com.bangkit.wastify.ui.screens.auth

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
import com.bangkit.wastify.databinding.FragmentRegisterBinding
import com.bangkit.wastify.ui.viewmodels.AuthViewModel
import com.bangkit.wastify.utils.Helper.isValidEmail
import com.bangkit.wastify.utils.Helper.toast
import com.bangkit.wastify.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Validating register result
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.registerFlow.collect { state ->
                    if (state != null) {
                        when(state) {
                            UiState.Loading -> showLoading(true)
                            is UiState.Failure -> {
                                showLoading(false)
                                toast(state.error.toString())
                            }
                            is UiState.Success -> {
                                showLoading(false)
                                toast(getString(R.string.msg_register_success))
                                findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                            }
                        }
                    }
                }
            }
        }

        // Do register process
        binding.btnRegister.setOnClickListener {
            register()
        }

        // Move to login page
        binding.btnLogin.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun register() {
        val nameEntry = binding.edtFullName.text.toString()
        val emailEntry = binding.edtEmail.text.toString()
        val passwordEntry = binding.edtPassword.text.toString()
        val confirmPasswordEntry = binding.edtConfirmPassword.text.toString()

        // Checking the form requirement
        when {
            nameEntry.isEmpty() -> binding.edtFullName.error = getString(R.string.msg_field_required)
            emailEntry.isEmpty() -> binding.edtEmail.error = getString(R.string.msg_field_required)
            passwordEntry.isEmpty() -> binding.edtPassword.error = getString(R.string.msg_field_required)
            confirmPasswordEntry.isEmpty() -> binding.edtConfirmPassword.error = getString(R.string.msg_field_required)

            confirmPasswordEntry != passwordEntry -> binding.edtConfirmPassword.error = getString(R.string.msg_passwords_not_match)
            !isValidEmail(emailEntry) -> binding.edtEmail.error = getString(R.string.msg_input_valid_email)

            else -> authViewModel.register(nameEntry, emailEntry, passwordEntry)
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