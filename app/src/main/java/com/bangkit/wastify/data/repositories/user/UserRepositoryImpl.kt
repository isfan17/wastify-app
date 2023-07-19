package com.bangkit.wastify.data.repositories.user

import android.graphics.Bitmap
import android.net.Uri
import com.bangkit.wastify.data.db.dao.ArticleDao
import com.bangkit.wastify.data.db.dao.UserDao
import com.bangkit.wastify.data.db.entities.asDomainModel
import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.data.model.Result
import com.bangkit.wastify.data.model.User
import com.bangkit.wastify.data.model.asEntityModel
import com.bangkit.wastify.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val userDao: UserDao,
    private val articleDao: ArticleDao,
) : UserRepository {

    override suspend fun getSavedResults(): Flow<UiState<List<Result>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSavedArticles() = articleDao.getSavedArticles().map { it.asDomainModel() }

    override suspend fun setArticleBookmark(article: Article, bookmarkState: Boolean) {
        article.isBookmarked = bookmarkState
        articleDao.updateArticle(article.asEntityModel())
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