package com.bangkit.wastify.ui.views.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.wastify.data.repositories.main.MainRepository
import com.bangkit.wastify.data.repositories.user.UserRepository
import com.bangkit.wastify.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    userRepository: UserRepository,
    private val mainRepository: MainRepository,
): ViewModel() {

    val userData = userRepository.getUser()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = null)

    init {
        viewModelScope.launch {
            fetchSavedResults()
            fetchTypes()
            fetchCategories()
            fetchArticles()
        }
    }

    val types = mainRepository.getTypes()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = null)

    val categories = mainRepository.getCategories()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = null)

    val articles = mainRepository.getArticles()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = null)

    val results = mainRepository.getSavedResults()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = null)

    private val _fetchTypesResult = MutableStateFlow<UiState<String>?>(UiState.Loading)
    val fetchTypesResult: StateFlow<UiState<String>?> = _fetchTypesResult

    private val _fetchCategoriesResult = MutableStateFlow<UiState<String>?>(UiState.Loading)
    val fetchCategoriesResult: StateFlow<UiState<String>?> = _fetchCategoriesResult

    private val _fetchArticlesResult = MutableStateFlow<UiState<String>?>(UiState.Loading)
    val fetchArticlesResult: StateFlow<UiState<String>?> = _fetchArticlesResult

    private val _fetchSavedResultsResult = MutableStateFlow<UiState<String>?>(UiState.Loading)
    val fetchSavedResultsResult: StateFlow<UiState<String>?> = _fetchSavedResultsResult

    private fun fetchTypes() = viewModelScope.launch {
        val result = mainRepository.fetchTypes()
        _fetchTypesResult.value = result
    }

    private fun fetchCategories() = viewModelScope.launch {
        val result = mainRepository.fetchCategories()
        _fetchCategoriesResult.value = result
    }

    private fun fetchArticles() = viewModelScope.launch {
        val result = mainRepository.fetchArticles()
        _fetchArticlesResult.value = result
    }

    private fun fetchSavedResults() = viewModelScope.launch {
        val result = mainRepository.fetchResults()
        _fetchSavedResultsResult.value = result
    }
}