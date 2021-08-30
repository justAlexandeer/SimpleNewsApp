package com.github.justalexandeer.simplenewsapp.ui.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.databinding.NewLoadStateViewItemBinding

class NewsLoadStateViewHolder (
    val binding: NewLoadStateViewItemBinding,
    val view: View,
    retry: () -> Unit,
) : RecyclerView.ViewHolder(view) {

    init {
        Log.i(TAG, "init: viewholder create")
        binding.retryButton.setOnClickListener {
            retry.invoke()
        }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            if (loadState.error.message == null) {
                binding.errorMsg.text =
                    view.context.resources.getString(R.string.retryMessage)
            } else {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        private const val TAG = "NewsLoadStateViewHolder"
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