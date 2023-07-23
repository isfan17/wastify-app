package com.bangkit.wastify.ui.views.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.wastify.data.repositories.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(userRepository: UserRepository): ViewModel() {

    val userFlow = userRepository.getUser()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = null)
}