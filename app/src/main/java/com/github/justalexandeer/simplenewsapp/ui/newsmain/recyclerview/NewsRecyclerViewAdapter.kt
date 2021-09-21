package com.github.justalexandeer.simplenewsapp.ui.newsmain.recyclerview

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.justalexandeer.simplenewsapp.ui.view.ArticleViewHolder
import com.github.justalexandeer.simplenewsapp.ui.view.NewsFooterViewHolder

class NewsRecyclerViewAdapter : ListAdapter<DataItem, RecyclerView.ViewHolder> (NewsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ITEM_VIEW_TYPE_HEADER -> NewsFooterViewHolder.create(parent)
            ITEM_VIEW_TYPE_ITEM -> ArticleViewHolder.create(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is NewsFooterViewHolder -> {
                val nameTheme = getItem(position) as DataItem.NewTheme
                holder.bind(nameTheme.theme)
            }
            is ArticleViewHolder -> {
                val dataItem = getItem(position) as DataItem.NewItem
                val articleDb = dataItem.articleDb
                holder.bind(articleDb)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is DataItem.NewItem -> ITEM_VIEW_TYPE_ITEM
            is DataItem.NewTheme -> ITEM_VIEW_TYPE_HEADER
        }
    }

    companion object {
        private const val TAG = "NewsRecyclerViewAdapter"
        private const val ITEM_VIEW_TYPE_HEADER = 0
        private const val ITEM_VIEW_TYPE_ITEM = 1
    }


}

class NewsDiffUtil: DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}