package com.bangkit.wastify.data.repositories.main

import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.data.model.Category
import com.bangkit.wastify.data.model.CategoryAndMethods
import com.bangkit.wastify.data.model.Type
import com.bangkit.wastify.data.model.TypeAndCategories
import com.bangkit.wastify.utils.UiState
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun getTypes(): Flow<List<Type>>
    suspend fun getCategories(): Flow<List<Category>>
    suspend fun getArticles(): Flow<List<Article>>

    suspend fun fetchTypes(): UiState<String>
    suspend fun fetchCategories(): UiState<String>
    suspend fun fetchArticles(): UiState<String>

    suspend fun getType(id: String): Flow<TypeAndCategories>
    suspend fun getCategory(id: String): Flow<CategoryAndMethods>
    suspend fun getArticle(id: String): Flow<Article>
}