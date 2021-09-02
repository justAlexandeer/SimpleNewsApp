package com.github.justalexandeer.simplenewsapp.repository

import com.github.justalexandeer.simplenewsapp.api.util.ConverterResponse
import com.github.justalexandeer.simplenewsapp.api.NewsApi
import com.github.justalexandeer.simplenewsapp.data.network.response.SuccessResponse
import com.github.justalexandeer.simplenewsapp.data.network.response.Result
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val converterResponse: ConverterResponse
) {

    suspend fun getNews(query:String, position: Int) : SuccessResponse {
        delay(2000)
        return when ( val result = converterResponse.createCall<SuccessResponse> { newsApi.test(query,
            MainRepository.NETWORK_PAGE_SIZE, position) }) {
                is Result.Success -> result.data
                is Result.Error -> throw result.error
        }
    }

    suspend fun getNewsTest(query: String, position: Int): Result<SuccessResponse> {
        delay(2000)
        return converterResponse.createCall<SuccessResponse> { newsApi.test(query,
            MainRepository.NETWORK_PAGE_SIZE, position) }
    }
}