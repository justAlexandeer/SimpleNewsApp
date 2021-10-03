package com.github.justalexandeer.simplenewsapp.repository

import com.github.justalexandeer.simplenewsapp.api.util.ConverterResponse
import com.github.justalexandeer.simplenewsapp.api.NewsApi
import com.github.justalexandeer.simplenewsapp.data.model.FilterSettings
import com.github.justalexandeer.simplenewsapp.data.model.Language
import com.github.justalexandeer.simplenewsapp.data.network.response.SuccessArticlesResponse
import com.github.justalexandeer.simplenewsapp.data.network.response.Result
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val converterResponse: ConverterResponse,
) {

    suspend fun getNews(
        query: String,
        position: Int,
        filterSettings: FilterSettings
    ): SuccessArticlesResponse {
        delay(2000)
        return when (val result = converterResponse.createCall {
            val dateFrom = filterSettings.dateFrom
            val dateTo = filterSettings.dateTo
            val language =
                if (filterSettings.language == Language.DEFAULT)
                    ""
                else
                    filterSettings.language.name.lowercase()
            val sortBy = filterSettings.sortBy.toString()
            newsApi.getFilteredNews(
                query,
                MainRepository.NETWORK_PAGE_SIZE,
                position,
                dateFrom,
                dateTo,
                language,
                sortBy
            )
        }) {
            is Result.Success -> result.data
            is Result.Error -> throw result.error
        }
    }

    suspend fun getMainNews(
        query: String,
        position: Int,
        pageSize: Int,
    ): Result<SuccessArticlesResponse> {
        delay(2000)
        return converterResponse.createCall {
            newsApi.getNewsByQuery(
                query,
                pageSize,
                position
            )
        }
    }
}