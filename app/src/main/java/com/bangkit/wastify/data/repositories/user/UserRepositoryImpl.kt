package com.bangkit.wastify.data.repositories.user

import android.graphics.Bitmap
import android.net.Uri
import com.bangkit.wastify.data.db.dao.ArticleDao
import com.bangkit.wastify.data.db.dao.ResultDao
import com.bangkit.wastify.data.db.dao.UserDao
import com.bangkit.wastify.data.db.entities.asDomainModel
import com.bangkit.wastify.data.db.entities.asNetworkModel
import com.bangkit.wastify.data.model.User
import com.bangkit.wastify.data.model.asEntityModel
import com.bangkit.wastify.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val dbReference: DatabaseReference,
    private val userDao: UserDao,
    private val resultDao: ResultDao,
    private val articleDao: ArticleDao,
) : UserRepository {

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

    override suspend fun logout(): UiState<String> {
        return try {
            // Delete user related data and save it in firebase realtime db
            manageUserRelatedData()

            // Clear user and auth data
            userDao.deleteUser()
            firebaseAuth.signOut()

            UiState.Success("Logout Successful")
        } catch (e: Exception) {
            UiState.Failure(e.message)
        }
    }

    override fun getUser(): Flow<User?> {
        return userDao.getUser().map { userEntity ->
            userEntity?.asDomainModel()
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

                userDao.updateUser(objUser.asEntityModel())
            }
            UiState.Success("Profile has been saved")
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

    private suspend fun getUserImageUrl(id: String): String? {
        val imgRef = firebaseStorage.reference.child("user_img/$id/")

        return try {
            val downloadUrl: Uri = imgRef.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) { null }
    }

    private suspend fun manageUserRelatedData() {
        firebaseAuth.currentUser?.let {  firebaseUser ->
            // Store user saved articles data in firebase realtime db
            val savedArticlesRef = dbReference.child("saved_articles").child(firebaseUser.uid)
            savedArticlesRef.removeValue()
            val savedArticlesMap = HashMap<String, Boolean>()
            val savedArticles = articleDao.getSavedArticles().take(1).single()
            for (article in savedArticles) {
                savedArticlesMap[article.id] = true
            }
            savedArticlesRef.setValue(savedArticlesMap)

            // Clear articles data in local db
            articleDao.clearArticles()

            // Store user saved results data in firebase realtime db
            val savedResultsRef = dbReference.child("saved_results").child(firebaseUser.uid)
            savedResultsRef.removeValue()
            val results = resultDao.getResults().take(1).single()
            val networkResults = results.asNetworkModel()
            savedResultsRef.setValue(networkResults)

            // Clear results data in local db
            resultDao.clearResults()
        }
    }
}