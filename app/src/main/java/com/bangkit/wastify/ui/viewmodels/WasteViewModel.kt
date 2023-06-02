package com.bangkit.wastify.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.data.model.Category
import com.bangkit.wastify.data.repositories.waste.WasteRepository
import com.bangkit.wastify.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WasteViewModel @Inject constructor(
    private val repository: WasteRepository
): ViewModel() {

    private val _categoriesFlow = MutableStateFlow<UiState<List<Category>>?>(null)
    val categoriesFlow: StateFlow<UiState<List<Category>>?> = _categoriesFlow

    private val _articlesFlow = MutableStateFlow<UiState<List<Article>>?>(null)
    val articlesFlow: StateFlow<UiState<List<Article>>?> = _articlesFlow

    fun getCategories() = viewModelScope.launch {
        _categoriesFlow.value = UiState.Loading
        repository.getCategories { _categoriesFlow.value = it }
    }

    fun getArticles() = viewModelScope.launch {
        _articlesFlow.value = UiState.Loading
        repository.getArticles { _articlesFlow.value = it }
    }
}