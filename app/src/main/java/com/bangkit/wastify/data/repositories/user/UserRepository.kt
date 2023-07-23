package com.bangkit.wastify.data.repositories.user

import android.graphics.Bitmap
import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.data.model.Result
import com.bangkit.wastify.data.model.User
import com.bangkit.wastify.utils.UiState
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun login(email: String, password: String): UiState<User>
    suspend fun register(name: String, email: String, password: String): UiState<User>
    suspend fun logout(): UiState<String>

    fun getUser(): Flow<User?>
    suspend fun updateProfile(name: String, email: String, img: Bitmap?): UiState<String>
    suspend fun forgotPassword(email: String): UiState<String>
}