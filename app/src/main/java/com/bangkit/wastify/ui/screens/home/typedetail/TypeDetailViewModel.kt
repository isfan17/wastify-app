package com.bangkit.wastify.ui.screens.home.typedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.wastify.data.model.TypeAndCategories
import com.bangkit.wastify.data.repositories.main.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TypeDetailViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    private val _type = MutableStateFlow<TypeAndCategories?>(null)
    val type : StateFlow<TypeAndCategories?> = _type

    fun getType(id: String) = viewModelScope.launch {
        mainRepository.getType(id).collect { _type.value = it }
    }
}