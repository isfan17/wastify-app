package com.bangkit.wastify.data.repositories.auth

import android.net.Uri
import com.bangkit.wastify.data.db.dao.ResultDao
import com.bangkit.wastify.data.db.dao.UserDao
import com.bangkit.wastify.data.model.User
import com.bangkit.wastify.data.model.asEntityModel
import com.bangkit.wastify.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val userDao: UserDao,
    private val resultDao: ResultDao,
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): UiState<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val imageUrl = getUserImageUrl(result.user!!.uid)

            val objUser = User(
                id = result.user!!.uid,
                name = result.user!!.displayName!!,
                email = result.user!!.email!!,
                imageUrl = imageUrl
            )

            userDao.insertUser(objUser.asEntityModel())
            UiState.Success(objUser)
        } catch (e: Exception) {
            UiState.Failure(e.message)
        }
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): UiState<User> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()

            val objUser = User(
                id = result.user!!.uid,
                name = result.user!!.displayName!!,
                email = result.user!!.email!!
            )
            userDao.insertUser(objUser.asEntityModel())
            UiState.Success(objUser)
        } catch (e: Exception) {
            UiState.Failure(e.message)
        }
    }

    override suspend fun logout() {
        resultDao.clearResults()
        userDao.deleteUser()
        firebaseAuth.signOut()
    }

    override suspend fun forgotPassword(email: String): UiState<String> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            UiState.Success("Email has been sent")
        } catch (e: Exception) {
            UiState.Failure(e.message)
        }
    }

    private suspend fun getUserImageUrl(id: String): String? {
        val imgRef = firebaseStorage.reference.child("user_img/$id/")

        return try {
            val downloadUrl: Uri = imgRef.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            null
        }
    }
}