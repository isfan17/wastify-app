package com.bangkit.wastify.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.data.model.Category
import com.bangkit.wastify.data.model.Result
import com.bangkit.wastify.data.model.Type
import com.bangkit.wastify.data.repositories.identify.IdentifyRepository
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
    private val identifyRepository: IdentifyRepository,
): ViewModel() {

    val userData = userRepository.getUser()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = null)

    private val _userResults = MutableStateFlow<UiState<List<Result>>?>(null)
    val userResults : StateFlow<UiState<List<Result>>?> = _userResults

    fun getUserResults() = viewModelScope.launch {
        identifyRepository.getResults().collect {
            _userResults.value = it
        }
    }

    init {
        viewModelScope.launch {
            fetchTypes()
            fetchCategories()
            fetchArticles()
        }
    }

    private val _types = MutableStateFlow<List<Type>?>(null)
    val types : StateFlow<List<Type>?> = _types

    private val _categories = MutableStateFlow<List<Category>?>(null)
    val categories : StateFlow<List<Category>?> = _categories

    private val _articles = MutableStateFlow<List<Article>?>(null)
    val articles : StateFlow<List<Article>?> = _articles

    fun getTypes() = viewModelScope.launch {
        mainRepository.getTypes().collect { _types.value = it }
    }

    fun getCategories() = viewModelScope.launch {
        mainRepository.getCategories().collect { _categories.value = it }
    }

    fun getArticles() = viewModelScope.launch {
        mainRepository.getArticles().collect { _articles.value = it }
    }

    private val _fetchTypesResult = MutableStateFlow<UiState<String>?>(UiState.Loading)
    val fetchTypesResult: StateFlow<UiState<String>?> = _fetchTypesResult

    private val _fetchCategoriesResult = MutableStateFlow<UiState<String>?>(UiState.Loading)
    val fetchCategoriesResult: StateFlow<UiState<String>?> = _fetchCategoriesResult

    private val _fetchArticlesResult = MutableStateFlow<UiState<String>?>(UiState.Loading)
    val fetchArticlesResult: StateFlow<UiState<String>?> = _fetchArticlesResult

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
}