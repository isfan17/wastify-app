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
import com.bangkit.wastify.databinding.FragmentLoginBinding
import com.bangkit.wastify.ui.viewmodels.AuthViewModel
import com.bangkit.wastify.ui.components.LoadingDialog
import com.bangkit.wastify.utils.Helper.toast
import com.bangkit.wastify.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loadingDialog = LoadingDialog(requireActivity())

        // Validating login result
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.loginFlow.collect { state ->
                    if (state != null) {
                        when (state) {
                            UiState.Loading -> loadingDialog.show()
                            is UiState.Failure -> {
                                loadingDialog.dismiss()
                                toast(state.error.toString())
                            }
                            is UiState.Success -> {
                                loadingDialog.dismiss()
                                toast(getString(R.string.msg_login_success))
                                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                            }
                        }
                    }
                }
            }
        }

        // Do login process
        binding.btnLogin.setOnClickListener {
            logIn()
        }

        // Move to register page
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun logIn() {
        val emailEntry = binding.edtEmail.text.toString()
        val passwordEntry = binding.edtPassword.text.toString()

        when {
            emailEntry.isEmpty() -> binding.edtEmail.error = getString(R.string.msg_field_required)
            passwordEntry.isEmpty() -> binding.edtPassword.error = getString(R.string.msg_field_required)

            else -> authViewModel.login(emailEntry, passwordEntry)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}