package com.github.justalexandeer.simplenewsapp.ui.newsline

import androidx.paging.PagingData
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.ui.base.UiEffect
import com.github.justalexandeer.simplenewsapp.ui.base.UiEvent
import com.github.justalexandeer.simplenewsapp.ui.base.UiState

class ContractNewsLine {

    sealed class Event: UiEvent {
        data class GetNews(val query: String) : Event()
    }

    sealed class State : UiState {
        object Idle : State()
        data class PagingDataState(val listNews: PagingData<ArticleDb>) : State()
    }

    sealed class Effect : UiEffect {
        data class ShowToast(val message: String) : Effect()
    }
}