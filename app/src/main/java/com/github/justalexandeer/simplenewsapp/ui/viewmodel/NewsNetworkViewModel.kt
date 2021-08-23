package com.github.justalexandeer.simplenewsapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.github.justalexandeer.simplenewsapp.data.models.Articles
import com.github.justalexandeer.simplenewsapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsNetworkViewModel @Inject constructor(
    private val mainRepository: MainRepository
): ViewModel() {

    var listArticles: MutableLiveData<PagingData<Articles>> = MutableLiveData()

    fun getAllNews() {
        viewModelScope.launch {
            mainRepository.getAllNews(MainRepository.apiKey)
                .collect {
                    listArticles.value = it
                }
        }
    }

    companion object {
        private const val TAG = "NewsNetworkViewModel"
    }
}