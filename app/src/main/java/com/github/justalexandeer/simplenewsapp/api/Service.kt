package com.github.justalexandeer.simplenewsapp.api

import com.github.justalexandeer.simplenewsapp.api.interceptor.ApiQueryInterceptor
import com.github.justalexandeer.simplenewsapp.api.interceptor.LoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class Service {
    private val retrofit = Retrofit.Builder()
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpClient)
        .baseUrl("https://newsapi.org/")
        .build()

    companion object {
        val retrofit: NewsApi by lazy {Service().retrofit.create(NewsApi::class.java)}
        private val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(LoggingInterceptor())
            .addInterceptor(ApiQueryInterceptor())
            .build()

    }
}