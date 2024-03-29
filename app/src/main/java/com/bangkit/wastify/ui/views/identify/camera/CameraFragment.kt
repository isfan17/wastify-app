package com.bangkit.wastify.ui.views.identify.camera

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bangkit.wastify.R
import com.bangkit.wastify.databinding.FragmentCameraBinding
import com.bangkit.wastify.ui.components.LoadingDialog
import com.bangkit.wastify.utils.Helper.createPhotoFile
import com.bangkit.wastify.utils.Helper.rotateBitmap
import com.bangkit.wastify.utils.Helper.toast
import com.bangkit.wastify.utils.Helper.uriToBitmap
import com.bangkit.wastify.utils.UiState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CameraViewModel by viewModels()

    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            toast(getString(R.string.msg_error_camera_access))
            findNavController().navigateUp()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission.launch(Manifest.permission.CAMERA)
        loadingDialog = LoadingDialog(this)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.identificationFlow.collectLatest {
                    when (it) {
                        UiState.Loading -> {}
                        is UiState.Failure -> {
                            loadingDialog.dismiss()
                            toast(it.error.toString())
                        }
                        is UiState.Success -> {
                            loadingDialog.dismiss()
                            val action =
                                CameraFragmentDirections.actionCameraFragmentToResultFragment(
                                    result = it.data,
                                    isDetail = false
                                )
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }

        // Switch camera (back or front)
        binding.btnSwitch.setOnClickListener {
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }
        // Open gallery
        binding.btnGallery.setOnClickListener { startGallery() }
        // Snap a picture
        binding.btnShutter.setOnClickListener { takePhoto() }
        // Handle back action
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        // Btn info
        binding.btnInfo.setOnClickListener {
            context?.let { ctx ->
                MaterialAlertDialogBuilder(ctx)
                    .setTitle(getString(R.string.more_accurate_result))
                    .setMessage(getString(R.string.msg_more_accurate))
                    .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setTargetRotation(requireActivity().windowManager.defaultDisplay.rotation)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                toast(getString(R.string.msg_fail_show_camera))
            }
        }, ContextCompat.getMainExecutor(requireActivity()))
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
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val imgBitmap = uriToBitmap(requireContext(), selectedImg)
            viewModel.identifyWaste(imgBitmap!!)
        }
    }

    private fun takePhoto() {
        loadingDialog.show()

        val imageCapture = imageCapture ?: return
        val photoFile = createPhotoFile(requireActivity().application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireActivity()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val isBackCamera: Int = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) 1 else -1
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                        val imgBitmap = rotateBitmap(
                            BitmapFactory.decodeFile(photoFile.path),
                            isBackCamera
                        )
                        viewModel.identifyWaste(imgBitmap)
                    }
                }

                override fun onError(exc: ImageCaptureException) {
                    toast(getString(R.string.msg_fail_capture_photo))
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}