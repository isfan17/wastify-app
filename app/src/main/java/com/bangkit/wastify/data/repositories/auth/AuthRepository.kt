package com.bangkit.wastify.data.repositories.auth

import android.graphics.Bitmap
import com.bangkit.wastify.data.model.User
import com.bangkit.wastify.utils.UiState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): UiState<User>
    suspend fun register(name: String, email: String, password: String): UiState<User>
    suspend fun forgotPassword(email: String): UiState<String>
    suspend fun updateProfile(name: String, email: String, img: Bitmap?): UiState<String>
    fun getUser(): Flow<User>
    suspend fun logout()
}