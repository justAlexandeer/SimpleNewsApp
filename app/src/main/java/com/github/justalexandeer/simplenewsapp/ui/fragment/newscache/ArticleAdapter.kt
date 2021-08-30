package com.github.justalexandeer.simplenewsapp.ui.fragment.newscache

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.ui.view.ArticleViewHolder

class ArticleAdapter : PagingDataAdapter<ArticleDb, ArticleViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        private const val TAG = "ArticleAdapter"
        private val diffCallback = object : DiffUtil.ItemCallback<ArticleDb>() {
            override fun areItemsTheSame(oldItem: ArticleDb, newItem: ArticleDb): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: ArticleDb, newItem: ArticleDb): Boolean =
                oldItem == newItem
        }
    }

}