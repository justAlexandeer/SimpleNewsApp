package com.github.justalexandeer.simplenewsapp.ui.newsline

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsLineViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

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