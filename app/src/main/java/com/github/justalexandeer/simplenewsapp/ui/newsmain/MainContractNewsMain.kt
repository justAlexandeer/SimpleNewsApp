package com.github.justalexandeer.simplenewsapp.ui.newsmain

import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.ui.base.UiEffect
import com.github.justalexandeer.simplenewsapp.ui.base.UiEvent
import com.github.justalexandeer.simplenewsapp.ui.base.UiState

class MainContractNewsMain {

    sealed class Event: UiEvent {
        object GetMainNews : Event()
    }

    data class State (
        val mainNewsState : MainNewsState
    ) : UiState

    sealed class MainNewsState {
        object Idle : MainNewsState()
        object Loading : MainNewsState()
        data class Error (val string: String): MainNewsState()
        data class Success (val listNews: List<ArticleDb>) : MainNewsState()
    }

    sealed class Effect : UiEffect {
        object ShowToast: Effect()
    }

}