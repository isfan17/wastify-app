package com.bangkit.wastify.data.repositories.auth

import android.graphics.Bitmap
import android.net.Uri
import com.bangkit.wastify.data.db.WastifyDao
import com.bangkit.wastify.data.model.User
import com.bangkit.wastify.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val wastifyDao: WastifyDao
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
            wastifyDao.insertUser(objUser)
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
            wastifyDao.insertUser(objUser)
            UiState.Success(objUser)
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
        email: String,
        img: Bitmap?
    ): UiState<String> {
        return try {
            val profileUpdates = userProfileChangeRequest { displayName = name }

            firebaseAuth.currentUser?.let { firebaseUser ->
                firebaseUser.updateProfile(profileUpdates)
                firebaseUser.updateEmail(email)

                val imageUrl = getUserImageUrl(firebaseUser.uid)
                val objUser = User(
                    id = firebaseUser.uid,
                    name = name,
                    email = email,
                    imageUrl = imageUrl
                )

                // Create img in storage
                if (img != null) {
                    val storageRef = firebaseStorage.reference.child("user_img/${firebaseUser.uid}")
                    val outputStream = ByteArrayOutputStream()
                    img.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    val compressedImg = outputStream.toByteArray()

                    val uploadTask = storageRef.putBytes(compressedImg)
                    val uploadResult = uploadTask.await()

                    if (uploadResult.task.isSuccessful) {
                        val downloadUrl = storageRef.downloadUrl.await()
                        objUser.imageUrl = downloadUrl.toString()
                    } else {
                        Timber.d("Failed to upload image")
                    }
                }

                wastifyDao.updateUser(objUser)
            }
            UiState.Success("Profile has been saved")
        } catch (e: Exception) {
            UiState.Failure(e.message)
        }
    }

    override fun getUser() = wastifyDao.getUser()

    override suspend fun logout() {
        wastifyDao.clearResults()
        wastifyDao.deleteUser()
        firebaseAuth.signOut()
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