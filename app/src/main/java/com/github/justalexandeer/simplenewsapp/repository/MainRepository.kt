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
import com.github.justalexandeer.simplenewsapp.data.cache.NetworkBoundResponse
import com.github.justalexandeer.simplenewsapp.data.cache.status.Status
import com.github.justalexandeer.simplenewsapp.data.db.AppDatabase
import com.github.justalexandeer.simplenewsapp.data.db.ArticleDao
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.data.db.entity.SourceDb
import com.github.justalexandeer.simplenewsapp.data.network.response.Article
import com.github.justalexandeer.simplenewsapp.data.network.response.SuccessResponse
import com.github.justalexandeer.simplenewsapp.data.network.response.Result
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import javax.security.auth.login.LoginException

@Singleton
class MainRepository @Inject constructor(
    val newsApi: NewsApi,
    val networkRepository: NetworkRepository,
    val articleDao: ArticleDao,
    val appDatabase: AppDatabase,
    @ApplicationContext val appContext: Context
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getAllNewsAndCache(query: String): Flow<PagingData<ArticleDb>> {
        val pagingSourceFactory = { appDatabase.articleDao().articlesByQuery(query) }

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
            ),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = ArticleRemoteMediator(
                query,
                appDatabase,
                networkRepository,
                appContext
            )

        ).flow
    }

    suspend fun getNews(query: String): Flow<Result<SuccessResponse>> {
        val result:Result<SuccessResponse> = networkRepository.getNewsTest(query, 1)

        return flow {
            emit(result)
        }
    }

    suspend fun getMainNews() {
        var autoIncrementId = 0
        object : NetworkBoundResponse<List<ArticleDb>, Result<SuccessResponse>>() {
            override suspend fun loadFromNetwork(): Result<SuccessResponse> {
                return networkRepository.getNewsTest("bitcoin", 1)
            }

            override suspend fun loadFromDb(): Flow<List<ArticleDb>> {
                return articleDao.getAllArticle()
            }

            override fun shodFetch(data: List<ArticleDb>?): Boolean {
                return true
            }

            override suspend fun saveToDb(data: List<ArticleDb>) {
                articleDao.insertAllMainArticle(data)
            }

            override fun mapData(result: Result<SuccessResponse>): List<ArticleDb> {
                val listArticleFromNetwork = (result as Result.Success<SuccessResponse>).data.articles
                val listArticleToDb = listArticleFromNetwork.map {
                    autoIncrementId++
                    val source =
                        SourceDb(
                            autoIncrementId.toLong(),
                            it.source.name
                        )
                    ArticleDb(
                        source,
                        it.author?:appContext.resources.getResourceName(R.string.unknownAuthor),
                        it.title,
                        it.description,
                        it.url,
                        it.urlToImage,
                        it.publishedAt,
                        it.content,
                        "bitcoin",
                        autoIncrementId.toLong()
                    )
                }
                return listArticleToDb
            }
        }.asFlow()
            .collect {
                when(it) {
                    is Status.Success -> {
                        Log.i(TAG, "getMainNews: Success")
                    }
                    is Status.Error -> {
                        Log.i(TAG, "getMainNews: Error")
                    }
                    is Status.Loading -> {
                        Log.i(TAG, "getMainNews: Loading")
                    }
                }
            }
    }


    companion object {
        private const val TAG = "MainRepository"
        const val NETWORK_PAGE_SIZE = 10
        val apiKey = "d83fc9b917ea4d8d99f4acae33467e07"
    }

}