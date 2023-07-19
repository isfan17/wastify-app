package com.bangkit.wastify.ui.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.wastify.data.model.Identification
import com.bangkit.wastify.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.bangkit.wastify.data.model.Result
import com.bangkit.wastify.data.repositories.identify.IdentifyRepository

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: IdentifyRepository
): ViewModel() {

    private val _identificationFlow = MutableStateFlow<UiState<Identification>?>(null)
    val identificationFlow : StateFlow<UiState<Identification>?> = _identificationFlow
    private val _saveResultFlow = MutableStateFlow<UiState<String>?>(null)
    val saveResultFlow: StateFlow<UiState<String>?> = _saveResultFlow
    private val _resultsFlow = MutableStateFlow<UiState<List<Result>>?>(null)
    val resultsFlow : StateFlow<UiState<List<Result>>?> = _resultsFlow

    fun classifyWaste(image: Bitmap) = viewModelScope.launch {
        _identificationFlow.value = UiState.Loading
        val result = repository.classifyWaste(image)
        _identificationFlow.value = result
    }
    fun saveResult(identification: Identification) = viewModelScope.launch {
        _saveResultFlow.value = UiState.Loading
        val result = repository.storeResult(identification)
        _saveResultFlow.value = result
    }
    fun getResults() = viewModelScope.launch {
        repository.getResults().collect {
            _resultsFlow.value = it
        }
    }
}