package com.bangkit.wastify.data.repositories.auth

import android.graphics.Bitmap
import com.bangkit.wastify.data.model.User
import com.bangkit.wastify.utils.UiState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): UiState<User>
    suspend fun register(name: String, email: String, password: String): UiState<User>
    suspend fun logout()
    suspend fun forgotPassword(email: String): UiState<String>
}