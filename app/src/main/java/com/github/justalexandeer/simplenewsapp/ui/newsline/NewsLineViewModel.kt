package com.github.justalexandeer.simplenewsapp.ui.newsline

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.github.justalexandeer.simplenewsapp.data.model.FilterSettings
import com.github.justalexandeer.simplenewsapp.repository.MainRepository
import com.github.justalexandeer.simplenewsapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class NewsLineViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val mainRepository: MainRepository
) : BaseViewModel<ContractNewsLine.Event, ContractNewsLine.State, ContractNewsLine.Effect>() {

    var isFirstLoading = MutableStateFlow(true)
    var currentFilterSettings = FilterSettings()

    val currentFilterSettings1 = MutableStateFlow(FilterSettings())
    private var _currentFilterSettings1 = currentFilterSettings1

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
            is ContractNewsLine.Event.SetFilterSettings -> {
                _currentFilterSettings1.value = event.filterSettings
            }
        }
    }

    private fun getArticles(query: String) {
        viewModelScope.launch {
            mainRepository.getAllNewsAndCache(query, currentFilterSettings1.value)
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