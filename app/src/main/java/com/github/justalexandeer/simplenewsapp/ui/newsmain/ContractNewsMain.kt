package com.github.justalexandeer.simplenewsapp.ui.newsmain

import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.ui.base.UiEffect
import com.github.justalexandeer.simplenewsapp.ui.base.UiEvent
import com.github.justalexandeer.simplenewsapp.ui.base.UiState

class ContractNewsMain {

    sealed class Event : UiEvent {
        object GetMainNews : Event()
    }

    /*data class State(
        val isIdle: Boolean = false,
        val isLoading: Boolean = false,
        val loading: List<ArticleDb>? = null,
        val success: List<ArticleDb>? = null,
        val error: String? = null
    ) : UiState
*/
    sealed class State : UiState {
        object Idle : State()
        data class Loading(val listNews: List<ArticleDb>?) : State()
        data class Success(val listNews: List<ArticleDb>) : State()
        data class Error(val errorMessage: String) : State()
    }

    sealed class Effect : UiEffect {
        data class ShowToast(val message: String) : Effect()
    }


}