package com.github.justalexandeer.simplenewsapp.ui.newsdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    val newsIsFavorite: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    fun isUserAddNewsToFavorite(title: String, content: String, url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.isUserAddNewsToFavorite(title, content, url).collect {
                newsIsFavorite.value = it != null
            }
        }
    }

    fun addNewToFavorite(article: ArticleDb) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.addNewToFavorite(article)
        }
    }

    fun removeNewFromFavorite(article: ArticleDb) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.removeNewFromFavorite(article)
        }
    }

}