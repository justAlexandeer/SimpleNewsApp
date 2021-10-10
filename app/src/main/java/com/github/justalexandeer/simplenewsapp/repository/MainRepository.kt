package com.github.justalexandeer.simplenewsapp.repository

import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.api.NewsApi
import com.github.justalexandeer.simplenewsapp.data.ArticleRemoteMediator
import com.github.justalexandeer.simplenewsapp.data.cache.NetworkBoundResource
import com.github.justalexandeer.simplenewsapp.data.cache.status.Status
import com.github.justalexandeer.simplenewsapp.data.db.AppDatabase
import com.github.justalexandeer.simplenewsapp.data.db.ArticleDao
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.data.model.FilterSettings
import com.github.justalexandeer.simplenewsapp.data.network.response.SuccessArticlesResponse
import com.github.justalexandeer.simplenewsapp.data.network.response.Result
import com.github.justalexandeer.simplenewsapp.ui.newsmain.NewsMainViewModel
import com.github.justalexandeer.simplenewsapp.util.DEFAULT_IMAGE_URL
import com.github.justalexandeer.simplenewsapp.util.MainNewsTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    val newsApi: NewsApi,
    val networkRepository: NetworkRepository,
    val articleDao: ArticleDao,
    val appDatabase: AppDatabase,
    @ApplicationContext val appContext: Context,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getAllNewsAndCache(
        query: String,
        filterSettings: FilterSettings
    ): Flow<PagingData<ArticleDb>> {
        val pagingSourceFactory =
            { appDatabase.articleDao().articlesByQuery(TYPE_ARTICLE_LINE, query) }
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
            ),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = ArticleRemoteMediator(
                query,
                filterSettings,
                appDatabase,
                networkRepository,
                appContext
            )
        ).flow
    }

    suspend fun getMainNews(setNewsTheme: Set<MainNewsTheme>): Flow<Status<out List<ArticleDb>>> {
        class MainNewsNetworkResource(query: String) :
            NetworkBoundResource<List<ArticleDb>, Result<SuccessArticlesResponse>>(query) {

            override suspend fun loadFromNetwork(query: String): Result<SuccessArticlesResponse> {
                return networkRepository.getMainNews(query, 1, NETWORK_PAGE_SIZE)
            }

            override suspend fun loadFromDb(
                articleType: String,
                query: String
            ): Flow<List<ArticleDb>> {
                return articleDao.getAllArticle(articleType, query)
            }

            override fun shodFetch(data: List<ArticleDb>?): Boolean {
                return true
            }

            override suspend fun saveToDb(data: List<ArticleDb>) {
                articleDao.insertAllMainArticle(data)
            }

            override suspend fun deleteOldArticle(articleType: String, query: String) {
                articleDao.clearArticles(articleType, query)
            }

            override fun mapData(
                result: Result<SuccessArticlesResponse>,
                query: String
            ): List<ArticleDb> {
                val listArticleFromNetwork =
                    (result as Result.Success<SuccessArticlesResponse>).data.articles
                return listArticleFromNetwork.map {
                    ArticleDb(
                        it.author ?: appContext.resources.getString(R.string.unknownAuthor),
                        it.title,
                        it.description,
                        it.url,
                        it.urlToImage ?: DEFAULT_IMAGE_URL,
                        it.publishedAt,
                        it.content,
                        query,
                        TYPE_ARTICLE
                    )
                }
            }
        }

        return flow {
            setNewsTheme.forEach {
                emit(MainNewsNetworkResource(it.toString()).asFlow())
            }
        }
            .flattenMerge()
    }

    fun isUserAddNewsToFavorite(title: String, content: String, url: String): Flow<ArticleDb?> {
        return appDatabase.articleDao()
            .getArticleInFavorite(TYPE_ARTICLE_FAVORITE, title, content, url)
    }

    suspend fun addNewToFavorite(article: ArticleDb) {
        val articleWithNewType = article.copy(type = TYPE_ARTICLE_FAVORITE, idArticle = 0L)
        appDatabase.articleDao().insertAll(mutableListOf(articleWithNewType))
    }

    suspend fun removeNewFromFavorite(article: ArticleDb) {
        appDatabase.articleDao().clearArticleFromFavorite(
            TYPE_ARTICLE_FAVORITE,
            article.title,
            article.content,
            article.url
        )
    }

    fun getFavoriteNews(): Flow<List<ArticleDb>> {
        return appDatabase.articleDao().getFavoriteNews(TYPE_ARTICLE_FAVORITE)
    }


    companion object {
        private const val TYPE_ARTICLE_LINE = "Line"
        private const val TYPE_ARTICLE_FAVORITE = "Favorite"
        private const val TAG = "MainRepository"
        const val NETWORK_PAGE_SIZE = 10
        val apiKey = "46ce799bc4b64d06b448284996a569b8"
    }

}