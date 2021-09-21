package com.github.justalexandeer.simplenewsapp.ui.newsmain.recyclerview

import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb

sealed class DataItem {
    abstract val id: Long
    data class NewItem(val articleDb: ArticleDb): DataItem() {
        override val id = articleDb.idArticle
    }
    data class NewTheme(val theme: String): DataItem() {
        override val id = NEW_THEME_ID
    }

    companion object {
        const val NEW_THEME_ID = 0L
    }
}