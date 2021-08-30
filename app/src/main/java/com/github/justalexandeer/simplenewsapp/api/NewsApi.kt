package com.github.justalexandeer.simplenewsapp.api

import com.github.justalexandeer.simplenewsapp.data.network.response.SuccessResponse
import com.github.justalexandeer.simplenewsapp.data.network.response.ErrorResponse
import com.github.justalexandeer.simplenewsapp.data.network.response.NetworkResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/everything")
    suspend fun getGetAllNews(
        @Query("q") q: String,
        @Query("apiKey") apiKey: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): SuccessResponse

    @GET("v2/everything")
    suspend fun test(
        @Query("q") q: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): NetworkResponse<SuccessResponse, ErrorResponse>

    @GET("v2/everything")
    fun getTest(
        @Query("q") q: String,
        @Query("apiKey") apiKey: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): Call<SuccessResponse>

}