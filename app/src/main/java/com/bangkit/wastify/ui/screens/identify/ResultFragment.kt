package com.bangkit.wastify.ui.screens.identify

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bangkit.wastify.R
import com.bangkit.wastify.databinding.FragmentResultBinding
import com.bangkit.wastify.utils.Helper.rotateBitmap
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val navArgs: ResultFragmentArgs by navArgs()

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
            binding.ivResult.setImageBitmap(imageResult)
        }

        binding.btnGoToHome.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_homeFragment)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}