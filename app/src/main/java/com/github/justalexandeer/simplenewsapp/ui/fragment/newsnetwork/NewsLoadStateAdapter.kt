package com.github.justalexandeer.simplenewsapp.ui.fragment.newsnetwork

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class NewsLoadStateAdapter(private val retry: () -> Unit): LoadStateAdapter<NewsLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NewsLoadStateViewHolder {
        return NewsLoadStateViewHolder.create(parent, retry)
    }

    override fun onBindViewHolder(holder: NewsLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

}