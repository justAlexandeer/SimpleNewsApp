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

    private var autoIncrementId = 0;

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleDb>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                autoIncrementId = 0;
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
                    articleDatabase.articleDao().clearArticles()
                }
                val prevKey = if (page == NEWS_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPagination) null else page + 1
                val articlesToDatabase = articles.map {
                    autoIncrementId++
                    val source =
                        SourceDb(
                            autoIncrementId.toLong(),
                            it.source.name
                        )
                    ArticleDb(
                        source,
                        it.author?:context.resources.getResourceName(R.string.unknownAuthor),
                        it.title,
                        it.description,
                        it.url,
                        it.urlToImage,
                        it.publishedAt,
                        it.content,
                        query,
                        autoIncrementId.toLong()
                    )
                }
                val keys = articlesToDatabase.map {
                    RemoteKeys(it.idArticle, prevKey, nextKey)
                }
                articleDatabase.articleDao().insertAll(articlesToDatabase)
                articleDatabase.remoteKeyDao().insertAll(keys)
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
                articleDatabase.remoteKeyDao().remoteKeysRepoId(it.source.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ArticleDb>): RemoteKeys? {
        return state.pages.firstOrNull() { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let {
                articleDatabase.remoteKeyDao().remoteKeysRepoId(it.source.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ArticleDb>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.source?.id?.let {
                articleDatabase.remoteKeyDao().remoteKeysRepoId(position.toLong())
            }
        }
    }

    companion object {
        private const val NEWS_STARTING_PAGE_INDEX = 1
        private var counter = 0
        private const val TAG = "ArticleRemoteMediator"
    }
}