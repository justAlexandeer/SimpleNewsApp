package com.github.justalexandeer.simplenewsapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.justalexandeer.simplenewsapp.api.NewsApi
import com.github.justalexandeer.simplenewsapp.data.NewsPagingSource
import com.github.justalexandeer.simplenewsapp.data.models.Articles
import com.github.justalexandeer.simplenewsapp.data.models.SuccessResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(val newsApi: NewsApi) {

    fun getAllNews(apiKey: String): Flow<PagingData<Articles>> {
        return Pager(
            config = PagingConfig(
                pageSize = NewsPagingSource.NETWORK_PAGE_SIZE
            ),
            pagingSourceFactory = { NewsPagingSource(newsApi) }
        ).flow
    }

    companion object {
        val apiKey = "d83fc9b917ea4d8d99f4acae33467e07"
    }

}