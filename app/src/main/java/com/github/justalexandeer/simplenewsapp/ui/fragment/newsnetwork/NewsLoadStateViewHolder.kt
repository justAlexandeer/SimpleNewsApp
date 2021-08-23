package com.github.justalexandeer.simplenewsapp.ui.fragment.newsnetwork

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.github.justalexandeer.simplenewsapp.databinding.NewLoadStateViewItemBinding
import com.github.justalexandeer.simplenewsapp.databinding.NewViewItemBinding

class NewsLoadStateViewHolder(
    val binding: NewLoadStateViewItemBinding,
    view: View,
    retry: () -> Unit,
) : RecyclerView.ViewHolder(view) {

    init {
        binding.retryButton.setOnClickListener {
            retry.invoke()
        }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.message
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): NewsLoadStateViewHolder {
            val binding = NewLoadStateViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return NewsLoadStateViewHolder(binding, binding.root, retry)
        }
    }


}