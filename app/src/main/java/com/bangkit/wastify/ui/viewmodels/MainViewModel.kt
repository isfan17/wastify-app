package com.bangkit.wastify.ui.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.data.model.Category
import com.bangkit.wastify.data.model.Identification
import com.bangkit.wastify.data.model.Type
import com.bangkit.wastify.data.repositories.main.MainRepository
import com.bangkit.wastify.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.bangkit.wastify.data.model.Result

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    private val _typesFlow = MutableStateFlow<List<Type>?>(null)
    val typesFlow : StateFlow<List<Type>?> = _typesFlow
    private val _categoriesFlow = MutableStateFlow<List<Category>?>(null)
    val categoriesFlow : StateFlow<List<Category>?> = _categoriesFlow

    private val _fetchTypesFlow = MutableStateFlow<UiState<String>?>(null)
    val fetchTypesFlow: StateFlow<UiState<String>?> = _fetchTypesFlow
    private val _fetchCategoriesFlow = MutableStateFlow<UiState<String>?>(null)
    val fetchCategoriesFlow: StateFlow<UiState<String>?> = _fetchCategoriesFlow

    private val _typeFlow = MutableStateFlow<Type?>(null)
    val typeFlow : StateFlow<Type?> = _typeFlow
    private val _categoryFlow = MutableStateFlow<Category?>(null)
    val categoryFlow : StateFlow<Category?> = _categoryFlow
    private val _articleFlow = MutableStateFlow<Article?>(null)
    val articleFlow : StateFlow<Article?> = _articleFlow

    val articlesFlow = repository.getArticles()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = null)

    fun getTypes() = viewModelScope.launch {
        repository.getTypes().collect {
            _typesFlow.value = it
        }
    }
    fun getCategories() = viewModelScope.launch {
        repository.getCategories().collect {
            _categoriesFlow.value = it
        }
    }

    fun fetchTypes() = viewModelScope.launch {
        _fetchTypesFlow.value = UiState.Loading
        val result = repository.fetchTypes()
        _fetchTypesFlow.value = result
    }
    fun fetchCategories() = viewModelScope.launch {
        _fetchCategoriesFlow.value = UiState.Loading
        val result = repository.fetchCategories()
        _fetchCategoriesFlow.value = result
    }

    fun getTypeById(id: String) = viewModelScope.launch {
        repository.getTypeById(id).collect {
            _typeFlow.value = it
        }
    }
    fun getCategoryById(id: String) = viewModelScope.launch {
        repository.getCategoryById(id).collect {
            _categoryFlow.value = it
        }
    }
    fun getArticleById(id: String) = viewModelScope.launch {
        repository.getArticleById(id).collect {
            _articleFlow.value = it
        }
    }

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