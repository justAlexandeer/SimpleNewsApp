package com.github.justalexandeer.simplenewsapp.api.interceptor

import com.github.justalexandeer.simplenewsapp.repository.MainRepository
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class ApiQueryInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val url: HttpUrl = request.url.newBuilder().addQueryParameter("apiKey", MainRepository.apiKey).build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}