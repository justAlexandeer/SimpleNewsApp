package com.github.justalexandeer.simplenewsapp.data

import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.api.NewsApi
import com.github.justalexandeer.simplenewsapp.data.db.AppDatabase
import com.github.justalexandeer.simplenewsapp.data.db.ArticleDao
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.data.db.entity.RemoteKeys
import com.github.justalexandeer.simplenewsapp.data.db.entity.SourceDb
import com.github.justalexandeer.simplenewsapp.data.network.response.Article
import com.github.justalexandeer.simplenewsapp.repository.NetworkRepository

@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator(
    private val query: String,
    private val articleDatabase: AppDatabase,
    private val networkRepository: NetworkRepository,
    private val context: Context
) : RemoteMediator<Int, ArticleDb>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleDb>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                Log.i(TAG, "load: LoadType.REFRESH")
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: NEWS_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                Log.i(TAG, "load: LoadType.PREPEND")
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                Log.i(TAG, "load: LoadType.APPEND")
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }

        try {
            Log.i(TAG, "load: page = $page")
            val apiResponse = networkRepository.getNews(query, page)
            val articles = apiResponse.articles

            val endOfPagination = articles.isEmpty()
            articleDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    articleDatabase.remoteKeyDao().clearRemoteKeys()
                    articleDatabase.articleDao().clearArticles(TYPE_ARTICLE)
                }
                val prevKey = if (page == NEWS_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPagination) null else page + 1
                val articlesToDatabase = articles.map {
                    ArticleDb(
                        it.author?:context.resources.getResourceName(R.string.unknownAuthor),
                        it.title,
                        it.description,
                        it.url,
                        it.urlToImage,
                        it.publishedAt,
                        it.content,
                        query,
                        TYPE_ARTICLE
                    )
                }
                val listId = articleDatabase.articleDao().insertAll(articlesToDatabase)
                val remoteKeys = listId.map {
                    RemoteKeys(it, prevKey, nextKey)
                }
                articleDatabase.remoteKeyDao().insertAll(remoteKeys)
            }
            return MediatorResult.Success(endOfPagination)
        } catch (exception: Exception) {
            Log.i(TAG, "load: ${exception.message}")
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ArticleDb>): RemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let {
                articleDatabase.remoteKeyDao().remoteKeysRepoId(it.idArticle)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ArticleDb>): RemoteKeys? {
        return state.pages.firstOrNull() { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let {
                articleDatabase.remoteKeyDao().remoteKeysRepoId(it.idArticle)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ArticleDb>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.idArticle.let {
                articleDatabase.remoteKeyDao().remoteKeysRepoId(position.toLong())
            }
        }
    }

    companion object {
        private const val NEWS_STARTING_PAGE_INDEX = 1
        private const val TAG = "ArticleRemoteMediator"
        const val TYPE_ARTICLE = "Line"
    }
}