package com.github.justalexandeer.simplenewsapp.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response = chain.proceed(request)
        //Log.i(TAG, "intercept: ${request.url}")

        Log.i(TAG, "intercept: ${request.url}")

        return response
    }

    companion object {
        private const val TAG = "LoggingInterceptor"
    }

}