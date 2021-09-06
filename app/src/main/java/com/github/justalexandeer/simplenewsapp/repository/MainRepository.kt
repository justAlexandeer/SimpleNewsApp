package com.github.justalexandeer.simplenewsapp.repository

import android.content.Context
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
import com.github.justalexandeer.simplenewsapp.data.network.response.SuccessArticlesResponse
import com.github.justalexandeer.simplenewsapp.data.network.response.Result
import com.github.justalexandeer.simplenewsapp.ui.newsmain.MainContractNewsMain
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

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

    suspend fun getNews(query: String): Flow<Result<SuccessArticlesResponse>> {
        val result: Result<SuccessArticlesResponse> = networkRepository.getNewsTest(query, 1)

        return flow {
            emit(result)
        }
    }

    // MainContract.MainNewsState
    suspend fun getMainNews() = flow {
        object : NetworkBoundResource<List<ArticleDb>, Result<SuccessArticlesResponse>>() {
            override suspend fun loadFromNetwork(): Result<SuccessArticlesResponse> {
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

            override suspend fun deleteOldArticle(articleType: String) {
                articleDao.clearArticles(articleType)
            }

            override fun mapData(result: Result<SuccessArticlesResponse>): List<ArticleDb> {
                val listArticleFromNetwork =
                    (result as Result.Success<SuccessArticlesResponse>).data.articles
                return listArticleFromNetwork.map {
                    ArticleDb(
                        it.author ?: appContext.resources.getResourceName(R.string.unknownAuthor),
                        it.title,
                        it.description,
                        it.url,
                        it.urlToImage,
                        it.publishedAt,
                        it.content,
                        "bitcoin",
                        TYPE_ARTICLE
                    )
                }
            }
        }.asFlow()
            .collect {
                when (it) {
                    is Status.Success -> {
                        emit(MainContractNewsMain.MainNewsState.Success(it.data))
                    }
                    is Status.Error -> {
                        emit(MainContractNewsMain.MainNewsState.Error
                            (it.message?:appContext.resources.getString(R.string.retryMessage)))
                    }
                    is Status.Loading -> {
                        emit(MainContractNewsMain.MainNewsState.Loading)
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