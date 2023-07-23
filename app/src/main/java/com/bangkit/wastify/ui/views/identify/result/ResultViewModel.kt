package com.bangkit.wastify.ui.views.identify.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.wastify.data.model.CategoryAndMethods
import com.bangkit.wastify.data.model.Result
import com.bangkit.wastify.data.model.TypeAndCategories
import com.bangkit.wastify.data.repositories.identify.IdentifyRepository
import com.bangkit.wastify.data.repositories.main.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val identifyRepository: IdentifyRepository,
): ViewModel() {

    private val _category = MutableStateFlow<CategoryAndMethods?>(null)
    val category: StateFlow<CategoryAndMethods?> = _category

    private val _type = MutableStateFlow<TypeAndCategories?>(null)
    val type: StateFlow<TypeAndCategories?> = _type

    fun getCategory(id: String) = viewModelScope.launch {
        mainRepository.getCategory(id).collect { _category.value = it }
    }

    fun getType(id: String) = viewModelScope.launch {
        mainRepository.getType(id).collect { _type.value = it }
    }

    fun saveResult(result: Result) {
        viewModelScope.launch {
            identifyRepository.storeResult(result)
        }
    }

    fun deleteResult(result: Result) {
        viewModelScope.launch {
            identifyRepository.deleteResult(result)
        }
    }
}