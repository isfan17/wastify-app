package com.bangkit.wastify.data.repositories.auth

import com.bangkit.wastify.utils.UiState
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): UiState<FirebaseUser>
    suspend fun register(name: String, email: String, password: String): UiState<FirebaseUser>
    suspend fun forgotPassword(email: String): UiState<String>
    suspend fun updateProfile(name: String, email: String): UiState<String>
    fun logout()
}