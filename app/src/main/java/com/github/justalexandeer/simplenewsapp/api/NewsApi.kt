package com.github.justalexandeer.simplenewsapp.api

import com.github.justalexandeer.simplenewsapp.data.network.response.SuccessArticlesResponse
import com.github.justalexandeer.simplenewsapp.data.network.response.ErrorResponse
import com.github.justalexandeer.simplenewsapp.data.network.response.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    
    @GET("v2/everything")
    suspend fun getNewsByQuery(
        @Query("q") q: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): NetworkResponse<SuccessArticlesResponse, ErrorResponse>
    
}