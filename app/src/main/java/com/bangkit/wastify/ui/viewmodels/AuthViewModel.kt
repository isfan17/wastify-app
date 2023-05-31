package com.bangkit.wastify.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.wastify.data.repositories.auth.AuthRepository
import com.bangkit.wastify.utils.UiState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private val _loginFlow = MutableStateFlow<UiState<FirebaseUser>?>(null)
    val loginFlow : StateFlow<UiState<FirebaseUser>?> = _loginFlow

    private val _registerFlow = MutableStateFlow<UiState<FirebaseUser>?>(null)
    val registerFlow : StateFlow<UiState<FirebaseUser>?> = _registerFlow

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        if (repository.currentUser != null) {
            _loginFlow.value = UiState.Success(repository.currentUser!!)
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value = UiState.Loading
        val result = repository.login(email, password)
        _loginFlow.value = result
    }

    fun register(name: String, email: String, password: String) = viewModelScope.launch {
        _registerFlow.value = UiState.Loading
        val result = repository.register(name, email, password)
        _registerFlow.value = result
    }

    fun logout() {
        repository.logout()
        _loginFlow.value = null
        _registerFlow.value = null
    }
}