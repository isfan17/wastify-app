package com.bangkit.wastify.ui.views.home.categorydetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.wastify.data.model.CategoryAndMethods
import com.bangkit.wastify.data.repositories.main.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDetailViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    private val _category = MutableStateFlow<CategoryAndMethods?>(null)
    val category : StateFlow<CategoryAndMethods?> = _category

    fun getCategory(id: String) = viewModelScope.launch {
        mainRepository.getCategory(id).collect { _category.value = it }
    }
}