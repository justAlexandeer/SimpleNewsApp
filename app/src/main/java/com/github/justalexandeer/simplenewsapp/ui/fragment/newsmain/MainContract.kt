package com.github.justalexandeer.simplenewsapp.ui.fragment.newsmain

import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb

class MainContract {

    sealed class Event {
        object GetMainNews : Event()
    }

    data class State (
        val mainNewsState : MainNewsState
    )

    sealed class MainNewsState {
        object Idle : MainNewsState()
        object Loading : MainNewsState()
        data class Success (val listNews: List<ArticleDb>) : MainNewsState()
    }

    sealed class Effect {
        object ShowToast: Effect()
    }

}