package com.bangkit.wastify.ui.screens.identify

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bangkit.wastify.R
import com.bangkit.wastify.databinding.FragmentResultBinding
import com.bangkit.wastify.ui.viewmodels.MainViewModel
import com.bangkit.wastify.utils.Helper.rotateBitmap
import com.bangkit.wastify.utils.Helper.toast
import com.bangkit.wastify.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val navArgs: ResultFragmentArgs by navArgs()

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageFile = navArgs.imageFile
        val imageUri = navArgs.imageUri
        val isBackCamera = navArgs.isBackCamera

        // Image is from Gallery
        if (isBackCamera == 0) {
            binding.ivResult.setImageURI(imageUri)
        }
        // Image is from Camera
        else {
            val imageResult = rotateBitmap(
                BitmapFactory.decodeFile(imageFile.path),
                isBackCamera
            )
            // TEST ML FUNCTION IN ANDROID
            mainViewModel.classifyWaste(imageResult)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    mainViewModel.typeFlow.collectLatest { type ->
                        if (type != null) {
                            binding.ivType.setImageResource(type.icon)
                            binding.tvType.text = type.name
                        }
                    }
                }

                launch {
                    mainViewModel.categoryFlow.collectLatest { category ->
                        if (category != null) {
                            binding.ivCategory.setImageResource(category.icon)
                            binding.tvCategory.text = category.name
                        }
                    }
                }

                launch {
                    mainViewModel.classificationFlow.collectLatest { state ->
                        if (state != null) {
                            when (state) {
                                UiState.Loading -> showLoading(true)
                                is UiState.Failure -> {
                                    showLoading(false)
                                    toast(state.error.toString())
                                }
                                is UiState.Success -> {
                                    showLoading(false)
                                    binding.ivResult.setImageBitmap(state.data.image)
                                    binding.tvResult.text = state.data.result

                                    mainViewModel.getTypeById(state.data.typeId)
                                    mainViewModel.getCategoryById(state.data.categoryId)

                                    binding.tvRecyclable.text = if (state.data.recyclable) getString(R.string.yes) else getString(R.string.no)
                                    binding.ivRecyclable.setImageResource(
                                        if (state.data.recyclable) R.drawable.recyclable_yes else R.drawable.recyclable_no
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        binding.btnGoToHome.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_homeFragment)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
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