package com.bangkit.wastify.ui.screens.settings

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bangkit.wastify.R
import com.bangkit.wastify.data.model.User
import com.bangkit.wastify.databinding.FragmentEditProfileBinding
import com.bangkit.wastify.ui.viewmodels.AuthViewModel
import com.bangkit.wastify.utils.Helper.isValidEmail
import com.bangkit.wastify.utils.Helper.toast
import com.bangkit.wastify.utils.UiState
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()
    private var user: User? = null

    private var bitmapEntry: Bitmap? = null

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Set user data
                launch {
                    authViewModel.userFlow.collectLatest {
                        user = it
                        setUserData()
                    }
                }

                // Check update result
                launch {
                    authViewModel.profileUpdatesFlow.collectLatest {
                        if (it != null) {
                            when (it) {
                                UiState.Loading -> showLoading(true)
                                is UiState.Failure -> {
                                    showLoading(false)
                                    toast(it.error.toString())
                                }
                                is UiState.Success -> {
                                    showLoading(false)
                                    toast(it.data)
                                    findNavController().navigateUp()
                                }
                            }
                        }
                    }
                }
            }
        }

        binding.btnUpdate.setOnClickListener { updateProfile() }

        binding.ivProfile.setOnClickListener { startGallery() }

        binding.btnForgot.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_forgotPasswordFragment)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
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
                authViewModel.updateProfile(nameEntry, emailEntry, bitmapEntry)
            }
        }
    }

    private fun setUserData() {
        binding.edtFullName.setText(user?.name)
        binding.edtEmail.setText(user?.email)

        if (user?.imageUrl == null) {
            binding.ivProfile.setImageResource(R.drawable.default_profile)
        } else {
            Glide.with(this)
                .load(user?.imageUrl)
                .into(binding.ivProfile)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.msg_choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            Glide.with(this)
                .load(selectedImg)
                .into(binding.ivProfile)


            selectedImg.let { uri ->
                val bitmap: Bitmap? = try {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    BitmapFactory.decodeStream(inputStream)
                } catch (e: Exception) {
                    toast(e.message.toString())
                    e.printStackTrace()
                    null
                }
                bitmapEntry = bitmap
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