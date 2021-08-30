package com.github.justalexandeer.simplenewsapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.data.network.response.Article
import com.github.justalexandeer.simplenewsapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsCacheViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    var listArticle: MutableLiveData<PagingData<ArticleDb>> = MutableLiveData()

    fun getArticles() {
        viewModelScope.launch {
            mainRepository.getAllNewsAndCache("bitcoin")
                .collect {
                    listArticle.value = it
                }
        }
    }

}