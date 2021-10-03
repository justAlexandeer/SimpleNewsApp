package com.github.justalexandeer.simplenewsapp.ui.newsmain

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.justalexandeer.simplenewsapp.api.interceptor.LoggingInterceptor
import com.github.justalexandeer.simplenewsapp.data.cache.status.Status
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.data.sharedpreferences.SharedPreferencesManager
import com.github.justalexandeer.simplenewsapp.repository.MainRepository
import com.github.justalexandeer.simplenewsapp.ui.base.BaseViewModel
import com.github.justalexandeer.simplenewsapp.util.MainNewsTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsMainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel<ContractNewsMain.Event, ContractNewsMain.State, ContractNewsMain.Effect>() {

    init {
        subscribeEvents()
    }

    override fun onCleared() {
        super.onCleared()
    }

    override fun createInitialState(): ContractNewsMain.State {
        return ContractNewsMain.State.Idle
    }

    override fun handleEvent(event: ContractNewsMain.Event) {
        when (event) {
            is ContractNewsMain.Event.GetMainNews -> {
                getNews()
            }
        }
    }

    private fun getNews() {
        val setOfTheme = getNewsTheme()
        val newsFromCache = mutableListOf<ArticleDb>()
        val newsFromNetwork = mutableListOf<ArticleDb>()
        var errorMessage: String? = null
        viewModelScope.launch {
            mainRepository.getMainNews(setOfTheme)
                .collect { status ->
                    when (status) {
                        is Status.Success -> {
                            newsFromNetwork.addAll(status.data)
                        }
                        is Status.Error -> {
                            status.message?.let {
                                errorMessage = status.message
                            }
                        }
                        is Status.Loading -> {
                            if (status.data == null) {
                                setState {
                                    ContractNewsMain.State.Loading(null)
                                }
                            } else {
                                status.data.let { newsFromCache.addAll(it) }
                                setState {
                                    ContractNewsMain.State.Loading(newsFromCache.toList())
                                }
                            }
                        }
                    }
                }
            if (errorMessage == null) {
                setState {
                    ContractNewsMain.State.Success(newsFromNetwork)
                }
            } else {
                setState {
                    ContractNewsMain.State.Error(errorMessage!!)
                }
            }
        }
    }

    private fun getNewsTheme(): Set<MainNewsTheme> {
        return sharedPreferencesManager.getSelectedTheme(SharedPreferencesManager.SELECTED_THEMES)
    }

    companion object {
        private const val TAG = "NewsMainViewModel"
    }
}