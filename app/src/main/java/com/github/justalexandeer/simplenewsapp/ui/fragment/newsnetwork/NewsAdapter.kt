package com.github.justalexandeer.simplenewsapp.ui.fragment.newsnetwork

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.github.justalexandeer.simplenewsapp.data.models.Articles

class NewsAdapter : PagingDataAdapter<Articles, NewsViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Articles>() {
            override fun areItemsTheSame(oldItem: Articles, newItem: Articles): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: Articles, newItem: Articles): Boolean =
                oldItem == newItem
        }
    }


}