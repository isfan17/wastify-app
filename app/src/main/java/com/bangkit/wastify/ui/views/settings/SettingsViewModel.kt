package com.bangkit.wastify.ui.views.settings

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.wastify.data.repositories.user.UserRepository
import com.bangkit.wastify.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
): ViewModel() {

    val userData = userRepository.getUser()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = null)

    private val _updateProfileResult = MutableStateFlow<UiState<String>?>(null)
    val updateProfileResult : StateFlow<UiState<String>?> = _updateProfileResult

    private val _logoutResult = MutableSharedFlow<UiState<String>>()
    val logoutResult = _logoutResult.asSharedFlow()

    fun updateProfile(name: String, email: String, img: Bitmap?) = viewModelScope.launch {
        _updateProfileResult.value = UiState.Loading
        val result = userRepository.updateProfile(name, email, img)
        _updateProfileResult.value = result
    }

    fun logout() {
        viewModelScope.launch {
            _logoutResult.emit(UiState.Loading)
            val result = userRepository.logout()
            _logoutResult.emit(result)
        }
    }
}