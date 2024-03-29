package com.bangkit.wastify.ui.views.home.articledetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.data.repositories.main.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    private val mainRepository: MainRepository,
): ViewModel() {

    private val _article = MutableStateFlow<Article?>(null)
    val article : StateFlow<Article?> = _article

    fun getArticle(id: String) = viewModelScope.launch {
        mainRepository.getArticle(id).collect { _article.value = it }
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            mainRepository.setArticleBookmark(article, true)
        }
    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            mainRepository.setArticleBookmark(article, false)
        }
    }
}