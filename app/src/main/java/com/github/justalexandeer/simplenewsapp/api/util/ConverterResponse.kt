package com.github.justalexandeer.simplenewsapp.api.util

import com.github.justalexandeer.simplenewsapp.data.network.response.Result
import com.github.justalexandeer.simplenewsapp.data.network.response.SuccessArticlesResponse
import com.github.justalexandeer.simplenewsapp.data.network.response.ErrorResponse
import com.github.justalexandeer.simplenewsapp.data.network.response.NetworkResponse
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConverterResponse @Inject constructor() {

    suspend fun <T: Any> createCall(call: suspend () -> NetworkResponse<T, ErrorResponse>): Result<T> {
        val response = call.invoke()
        when(response) {
            is NetworkResponse.Success -> {
                return Result.Success(response.body)
            }
            is NetworkResponse.ApiError -> {
                val errorResponse: ErrorResponse = response.body
                return Result.Error(Exception(errorResponse.message))
            }
            is NetworkResponse.NetworkError -> {
                return Result.Error(IOException(response.error.message))
            }
            is NetworkResponse.UnknownError -> {
                return Result.Error(Exception(response.error))
            }
        }
    }

    companion object {
        private const val TAG = "ConverterResponse"
    }

}