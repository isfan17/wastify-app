package com.bangkit.wastify.ui.screens.auth

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.wastify.data.model.User
import com.bangkit.wastify.data.repositories.auth.AuthRepository
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
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
): ViewModel() {

    private val _loginFlow = MutableStateFlow<UiState<User>?>(null)
    val loginFlow : StateFlow<UiState<User>?> = _loginFlow

    private val _registerFlow = MutableStateFlow<UiState<User>?>(null)
    val registerFlow : StateFlow<UiState<User>?> = _registerFlow

    private val _forgotPasswordFlow = MutableStateFlow<UiState<String>?>(null)
    val forgotPasswordFlow : StateFlow<UiState<String>?> = _forgotPasswordFlow

    private val _profileUpdatesFlow = MutableStateFlow<UiState<String>?>(null)
    val profileUpdatesFlow : StateFlow<UiState<String>?> = _profileUpdatesFlow

    val userFlow = userRepository.getUser()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = null)

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value = UiState.Loading
        val result = authRepository.login(email, password)
        _loginFlow.value = result
    }

    fun register(name: String, email: String, password: String) = viewModelScope.launch {
        _registerFlow.value = UiState.Loading
        val result = authRepository.register(name, email, password)
        _registerFlow.value = result
    }

    fun forgotPassword(email: String) = viewModelScope.launch {
        _forgotPasswordFlow.value = UiState.Loading
        val result = authRepository.forgotPassword(email)
        _forgotPasswordFlow.value = result
    }

    fun updateProfile(name: String, email: String, img: Bitmap?) = viewModelScope.launch {
        _profileUpdatesFlow.value = UiState.Loading
        val result = userRepository.updateProfile(name, email, img)
        _profileUpdatesFlow.value = result
    }

    fun logout() = viewModelScope.launch {
        authRepository.logout()
        _loginFlow.value = null
        _registerFlow.value = null
    }
}