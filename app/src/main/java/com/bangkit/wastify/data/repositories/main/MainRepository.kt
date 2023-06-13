package com.bangkit.wastify.data.repositories.main

import android.graphics.Bitmap
import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.data.model.Category
import com.bangkit.wastify.data.model.Classification
import com.bangkit.wastify.data.model.Type
import com.bangkit.wastify.utils.UiState
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getTypes(): Flow<List<Type>>
    fun getCategories(): Flow<List<Category>>
    fun getArticles(): Flow<List<Article>>

    suspend fun getTypeById(id: Int): Flow<Type>
    suspend fun getCategoryById(id: Int): Flow<Category>

    suspend fun classifyWaste(image: Bitmap): UiState<Classification>
}