package com.bangkit.wastify.ui.views.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.wastify.data.repositories.main.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    mainRepository: MainRepository
): ViewModel() {

    val results = mainRepository.getSavedResults()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = null)

    val articles = mainRepository.getSavedArticles()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = null)
}