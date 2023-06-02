package com.bangkit.wastify.data.repositories.waste

import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.data.model.Category
import com.bangkit.wastify.utils.DummyDataSource
import com.bangkit.wastify.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class WasteRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : WasteRepository {

    override suspend fun getCategories(result: (UiState<List<Category>>) -> Unit) {
        val categories = DummyDataSource.getCategories()
        result.invoke(
            UiState.Success(categories)
        )
    }

    override suspend fun getArticles(result: (UiState<List<Article>>) -> Unit) {
        val articles = DummyDataSource.getArticles()
        result.invoke(
            UiState.Success(articles)
        )
    }
}