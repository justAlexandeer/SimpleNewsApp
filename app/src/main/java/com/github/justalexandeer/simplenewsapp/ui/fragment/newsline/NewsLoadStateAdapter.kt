package com.github.justalexandeer.simplenewsapp.ui.fragment.newsline

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.github.justalexandeer.simplenewsapp.ui.view.NewsLoadStateViewHolder

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