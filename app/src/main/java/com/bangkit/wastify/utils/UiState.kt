package com.bangkit.wastify.utils

sealed class UiState<out R> {
    data class Success<out R>(val data: R): UiState<R>()
    data class Failure(val error: String?): UiState<Nothing>()
    object Loading: UiState<Nothing>()
}