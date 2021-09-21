package com.github.justalexandeer.simplenewsapp.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.justalexandeer.simplenewsapp.databinding.NewFooterViewItemBinding
import com.github.justalexandeer.simplenewsapp.databinding.NewViewItemBinding

class NewsFooterViewHolder(
    val view: View,
    val binding: NewFooterViewItemBinding
) : RecyclerView.ViewHolder(view) {

    fun bind(nameTheme: String) {
        binding.nameTheme.text = nameTheme
    }

    companion object {
        fun create(parent: ViewGroup): NewsFooterViewHolder {
            val binding = NewFooterViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return NewsFooterViewHolder(binding.root, binding)
        }
    }
}