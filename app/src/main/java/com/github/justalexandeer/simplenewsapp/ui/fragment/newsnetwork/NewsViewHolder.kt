package com.github.justalexandeer.simplenewsapp.ui.fragment.newsnetwork

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.justalexandeer.simplenewsapp.data.models.Articles
import com.github.justalexandeer.simplenewsapp.databinding.NewViewItemBinding

class NewsViewHolder(
    view: View,
    val binding: NewViewItemBinding
) : RecyclerView.ViewHolder(view) {

    fun bind(article: Articles?) {
        if(article == null) {
        } else {
            binding.newTitle.text = article.title
            binding.newDescription.text = article.description
        }
    }

    companion object {
        fun create(parent: ViewGroup): NewsViewHolder {
            val binding = NewViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return NewsViewHolder(binding.root, binding)
        }
    }
}