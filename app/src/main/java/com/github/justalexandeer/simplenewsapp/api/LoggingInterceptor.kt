package com.github.justalexandeer.simplenewsapp.api

import android.util.Log
import com.github.justalexandeer.simplenewsapp.data.ErrorResponse
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Callback
import retrofit2.HttpException
import java.lang.Exception

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