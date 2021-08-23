package com.github.justalexandeer.simplenewsapp.api

import okhttp3.OkHttp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

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
            .addInterceptor(LoggingInterceptor()).build()
    }
}