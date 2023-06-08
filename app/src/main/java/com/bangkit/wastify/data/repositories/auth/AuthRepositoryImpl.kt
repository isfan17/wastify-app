package com.bangkit.wastify.data.repositories.auth

import com.bangkit.wastify.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(
        email: String,
        password: String
    ): UiState<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            UiState.Success(result.user!!)
        } catch (e: Exception) {
            UiState.Failure(e.message)
        }
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): UiState<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            UiState.Success(result.user!!)
        } catch (e: Exception) {
            UiState.Failure(e.message)
        }
    }

    override suspend fun forgotPassword(email: String): UiState<String> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            UiState.Success("Email has been sent")
        } catch (e: Exception) {
            UiState.Failure(e.message)
        }
    }

    override suspend fun updateProfile(
        name: String,
        email: String
    ): UiState<String> {
        return try {
            val profileUpdates = userProfileChangeRequest { displayName = name }
            currentUser?.let {
                it.updateProfile(profileUpdates)
                it.updateEmail(email)
            }
            UiState.Success("Profile has been updated")
        } catch (e: Exception) {
            UiState.Failure(e.message)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}