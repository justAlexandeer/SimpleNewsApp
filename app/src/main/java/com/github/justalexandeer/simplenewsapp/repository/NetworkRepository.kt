package com.github.justalexandeer.simplenewsapp.repository

import com.github.justalexandeer.simplenewsapp.api.ConverterResponse
import com.github.justalexandeer.simplenewsapp.api.NewsApi
import com.github.justalexandeer.simplenewsapp.data.NewsPagingSource
import com.github.justalexandeer.simplenewsapp.data.models.SuccessResponse
import com.github.justalexandeer.simplenewsapp.data.models.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(private val newsApi: NewsApi) {

    private val converterResponse = ConverterResponse()

    suspend fun getNews(apiKey: String, position: Int) : SuccessResponse {
        return when ( val result = converterResponse.createCall<SuccessResponse> { newsApi.test("bitcoin", MainRepository.apiKey,
            NewsPagingSource.NETWORK_PAGE_SIZE, position) }) {
                is Result.Success -> result.data
                is Result.Error -> throw result.error
        }
    }

}