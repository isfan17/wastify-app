package com.bangkit.wastify.data.repositories.identify

import android.graphics.Bitmap
import com.bangkit.wastify.data.model.Result
import com.bangkit.wastify.utils.UiState

interface IdentifyRepository {

    suspend fun storeResult(result: Result)
    suspend fun deleteResult(result: Result)
    suspend fun identifyWaste(image: Bitmap): UiState<Result>
}