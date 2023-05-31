package com.bangkit.wastify.data.repositories.waste

import com.bangkit.wastify.data.model.Category
import com.bangkit.wastify.utils.UiState

interface WasteRepository {
    suspend fun getCategories(result: (UiState<List<Category>>) -> Unit)
}