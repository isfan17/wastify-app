package com.bangkit.wastify.data.repositories.user

import android.graphics.Bitmap
import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.data.model.Result
import com.bangkit.wastify.data.model.User
import com.bangkit.wastify.utils.UiState
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUser(): Flow<User?>
    suspend fun updateProfile(name: String, email: String, img: Bitmap?): UiState<String>

    suspend fun getSavedResults(): Flow<UiState<List<Result>>>

    suspend fun getSavedArticles(): Flow<List<Article>>
    suspend fun setArticleBookmark(article: Article, bookmarkState: Boolean)
}