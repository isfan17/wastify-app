package com.bangkit.wastify.ui.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.wastify.data.model.Category
import com.bangkit.wastify.data.model.Classification
import com.bangkit.wastify.data.model.Type
import com.bangkit.wastify.data.repositories.main.MainRepository
import com.bangkit.wastify.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    val typesFlow = repository.getTypes()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = null)

    val categoriesFlow = repository.getCategories()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = null)

    val articlesFlow = repository.getArticles()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = null)

    private val _typeFlow = MutableStateFlow<Type?>(null)
    val typeFlow : StateFlow<Type?> = _typeFlow

    private val _categoryFlow = MutableStateFlow<Category?>(null)
    val categoryFlow : StateFlow<Category?> = _categoryFlow

    fun getTypeById(id: Int) = viewModelScope.launch {
        repository.getTypeById(id).collect {
            _typeFlow.value = it
        }
    }

    fun getCategoryById(id: Int) = viewModelScope.launch {
        repository.getCategoryById(id).collect {
            _categoryFlow.value = it
        }
    }

    private val _classificationFlow = MutableStateFlow<UiState<Classification>?>(null)
    val classificationFlow : StateFlow<UiState<Classification>?> = _classificationFlow

    fun classifyWaste(image: Bitmap) = viewModelScope.launch {
        _classificationFlow.value = UiState.Loading
        val result = repository.classifyWaste(image)
        _classificationFlow.value = result
    }

}