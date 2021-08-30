package com.github.justalexandeer.simplenewsapp.repository

import com.github.justalexandeer.simplenewsapp.api.ConverterResponse
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

    suspend fun getNews(apiKey: String, position: Int) : SuccessResponse {
        delay(2000)
        return when ( val result = converterResponse.createCall<SuccessResponse> { newsApi.test("bitcoin",
            MainRepository.NETWORK_PAGE_SIZE, position) }) {
                is Result.Success -> result.data
                is Result.Error -> throw result.error
        }
    }

}