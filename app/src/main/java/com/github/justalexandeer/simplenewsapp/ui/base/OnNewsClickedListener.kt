package com.github.justalexandeer.simplenewsapp.ui.base

import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb

interface OnNewsClickedListener {
    fun onNewsClick(news: ArticleDb)
}
