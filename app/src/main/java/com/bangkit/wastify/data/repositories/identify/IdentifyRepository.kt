package com.bangkit.wastify.data.repositories.identify

import android.graphics.Bitmap
import com.bangkit.wastify.data.model.Identification
import com.bangkit.wastify.data.model.Result
import com.bangkit.wastify.utils.UiState
import kotlinx.coroutines.flow.Flow

interface IdentifyRepository {

    suspend fun getResults(): Flow<UiState<List<Result>>>
    suspend fun storeResult(identification: Identification): UiState<String>
    suspend fun classifyWaste(image: Bitmap): UiState<Identification>
}