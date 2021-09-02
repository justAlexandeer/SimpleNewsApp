package com.github.justalexandeer.simplenewsapp.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.justalexandeer.simplenewsapp.api.NewsApi
import com.github.justalexandeer.simplenewsapp.data.ArticleRemoteMediator
import com.github.justalexandeer.simplenewsapp.data.db.AppDatabase
import com.github.justalexandeer.simplenewsapp.data.db.ArticleDao
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.data.network.response.Article
import com.github.justalexandeer.simplenewsapp.data.network.response.SuccessResponse
import com.github.justalexandeer.simplenewsapp.data.network.response.Result
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
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

    suspend fun getNews(query: String): Flow<Result<SuccessResponse>> {
        val result:Result<SuccessResponse> = networkRepository.getNewsTest(query, 1)

        return flow {
            emit(result)
        }

    }

    companion object {
        private const val TAG = "MainRepository"
        const val NETWORK_PAGE_SIZE = 10
        val apiKey = "d83fc9b917ea4d8d99f4acae33467e07"
    }

}