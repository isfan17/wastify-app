package com.bangkit.wastify.data.repositories.main

import android.graphics.Bitmap
import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.data.model.Category
import com.bangkit.wastify.data.model.Identification
import com.bangkit.wastify.data.model.Type
import com.bangkit.wastify.utils.UiState
import kotlinx.coroutines.flow.Flow
import com.bangkit.wastify.data.model.Result

interface MainRepository {
    suspend fun getTypes(): Flow<List<Type>>
    suspend fun getCategories(): Flow<List<Category>>
    fun getArticles(): Flow<List<Article>>
    suspend fun fetchCategories(): UiState<String>
    suspend fun fetchTypes(): UiState<String>

    suspend fun getTypeById(id: String): Flow<Type>
    suspend fun getCategoryById(id: String): Flow<Category>
    suspend fun getArticleById(id: String): Flow<Article>

    suspend fun getResults(): Flow<UiState<List<Result>>>
    suspend fun storeResult(identification: Identification): UiState<String>
    suspend fun classifyWaste(image: Bitmap): UiState<Identification>
}