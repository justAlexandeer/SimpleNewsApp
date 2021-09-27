package com.github.justalexandeer.simplenewsapp.ui.newsline

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.repository.MainRepository
import com.github.justalexandeer.simplenewsapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.github.justalexandeer.simplenewsapp.ui.newsline.ContractNewsLine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NewsLineViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel<ContractNewsLine.Event, ContractNewsLine.State, ContractNewsLine.Effect>() {

    var isFirstLoading = MutableStateFlow(true)

    init {
        subscribeEvents()
    }

    override fun createInitialState(): ContractNewsLine.State {
        return ContractNewsLine.State.Idle
    }

    override fun handleEvent(event: ContractNewsLine.Event) {
        when(event) {
            is ContractNewsLine.Event.GetNews -> {
                getArticles(event.query)
            }
        }
    }

    private fun getArticles(query: String) {
        viewModelScope.launch {
            mainRepository.getAllNewsAndCache(query)
                .cachedIn(this)
                .collect {
                    setState {
                        ContractNewsLine.State.PagingDataState(it)
                    }
                }
        }
    }


    companion object {
        private const val TAG = "NewsLineViewModel"
    }
}