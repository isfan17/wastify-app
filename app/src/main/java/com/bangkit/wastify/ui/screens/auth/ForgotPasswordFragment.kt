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
import com.bangkit.wastify.databinding.FragmentForgotPasswordBinding
import com.bangkit.wastify.ui.components.LoadingDialog
import com.bangkit.wastify.utils.Helper
import com.bangkit.wastify.utils.Helper.toast
import com.bangkit.wastify.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loadingDialog = LoadingDialog(this)

        // Validating forgot password result
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.forgotPasswordFlow.collect { state ->
                    if (state != null) {
                        when (state) {
                            UiState.Loading -> loadingDialog.show()
                            is UiState.Failure -> {
                                loadingDialog.dismiss()
                                toast(state.error.toString())
                            }
                            is UiState.Success -> {
                                loadingDialog.dismiss()
                                toast(state.data)
                                findNavController().navigateUp()
                            }
                        }
                    }
                }
            }
        }

        binding.btnSend.setOnClickListener {
            sendForgotPasswordToEmail()
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun sendForgotPasswordToEmail() {
        val emailEntry = binding.edtEmail.text.toString()

        when {
            emailEntry.isEmpty() -> binding.edtEmail.error = getString(R.string.msg_field_required)
            !Helper.isValidEmail(emailEntry) -> binding.edtEmail.error = getString(R.string.msg_input_valid_email)
            else -> viewModel.forgotPassword(emailEntry)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}