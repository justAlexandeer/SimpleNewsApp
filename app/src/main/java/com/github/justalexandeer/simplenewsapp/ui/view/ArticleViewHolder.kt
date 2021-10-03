package com.github.justalexandeer.simplenewsapp.ui.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.DrawableTransformation
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.databinding.NewViewItemBinding
import com.github.justalexandeer.simplenewsapp.ui.base.OnNewsClickedListener
import com.github.justalexandeer.simplenewsapp.ui.newsline.recyclerview.ArticleAdapter
import com.github.justalexandeer.simplenewsapp.util.dateConverter

class ArticleViewHolder(
    private val view: View,
    private val binding: NewViewItemBinding
) : RecyclerView.ViewHolder(view){

    fun bind(article: ArticleDb?, listener: OnNewsClickedListener) {
        if (article == null) {
        } else {

            binding.constraintLayoutNews.setOnClickListener {
                listener.onNewsClick(article)
            }

            binding.newsTitle.text = article.title
            binding.newsDescription.text = article.description
            binding.newsAuthor.text = article.author
            binding.newsDate.text = dateConverter(article.publishedAt)

            Glide.with(itemView)
                .load(article.urlToImage)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error)
                .into(binding.newsImage)
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