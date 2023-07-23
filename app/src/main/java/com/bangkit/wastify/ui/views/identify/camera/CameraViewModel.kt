package com.bangkit.wastify.ui.views.identify.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.wastify.data.model.Result
import com.bangkit.wastify.data.repositories.identify.IdentifyRepository
import com.bangkit.wastify.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val identifyRepository: IdentifyRepository
): ViewModel() {

    private val _resultFlow = MutableSharedFlow<UiState<Result>>()
    val identificationFlow = _resultFlow.asSharedFlow()

    fun identifyWaste(image: Bitmap) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _resultFlow.emit(UiState.Loading)
                val result = identifyRepository.identifyWaste(image)
                _resultFlow.emit(result)
            }
        }
    }
}