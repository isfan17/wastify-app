package com.bangkit.wastify.ui.views.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.wastify.data.model.User
import com.bangkit.wastify.data.repositories.user.UserRepository
import com.bangkit.wastify.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
): ViewModel() {

    private val _loginFlow = MutableStateFlow<UiState<User>?>(null)
    val loginFlow : StateFlow<UiState<User>?> = _loginFlow

    private val _registerFlow = MutableStateFlow<UiState<User>?>(null)
    val registerFlow : StateFlow<UiState<User>?> = _registerFlow

    private val _forgotPasswordFlow = MutableStateFlow<UiState<String>?>(null)
    val forgotPasswordFlow : StateFlow<UiState<String>?> = _forgotPasswordFlow

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value = UiState.Loading
        val result = userRepository.login(email, password)
        _loginFlow.value = result
    }

    fun register(name: String, email: String, password: String) = viewModelScope.launch {
        _registerFlow.value = UiState.Loading
        val result = userRepository.register(name, email, password)
        _registerFlow.value = result
    }

    fun forgotPassword(email: String) = viewModelScope.launch {
        _forgotPasswordFlow.value = UiState.Loading
        val result = userRepository.forgotPassword(email)
        _forgotPasswordFlow.value = result
    }
}