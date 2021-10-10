package com.github.justalexandeer.simplenewsapp.ui.account.favorite

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.ui.base.OnNewsClickedListener
import com.github.justalexandeer.simplenewsapp.ui.view.ArticleViewHolder

class FavoriteNewsRecyclerViewAdapter(private val listener: OnNewsClickedListener) :
    ListAdapter<ArticleDb, ArticleViewHolder>(NewsDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        return holder.bind(article, listener)
    }
}

class NewsDiffUtil : DiffUtil.ItemCallback<ArticleDb>() {
    override fun areItemsTheSame(oldItem: ArticleDb, newItem: ArticleDb): Boolean {
        return oldItem.idArticle == newItem.idArticle
    }

    override fun areContentsTheSame(oldItem: ArticleDb, newItem: ArticleDb): Boolean {
        return oldItem == newItem
    }
}