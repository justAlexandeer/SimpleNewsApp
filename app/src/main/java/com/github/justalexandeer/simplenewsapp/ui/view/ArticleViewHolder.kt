package com.github.justalexandeer.simplenewsapp.ui.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.databinding.NewViewItemBinding

class ArticleViewHolder (
    val view: View,
    val binding: NewViewItemBinding
) : RecyclerView.ViewHolder(view) {

    fun bind(article: ArticleDb?) {
        if(article == null) {
        } else {
            binding.newId.text = article.idArticle.toString()
            binding.newTitle.text = article.title
            binding.newDescription.text = article.description
        }
    }

    companion object {
        fun create(parent: ViewGroup): ArticleViewHolder {
            val binding = NewViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ArticleViewHolder(binding.root, binding)
        }
    }

}