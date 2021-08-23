package com.github.justalexandeer.simplenewsapp.api

import android.util.Log
import com.github.justalexandeer.simplenewsapp.data.models.Result
import com.github.justalexandeer.simplenewsapp.data.models.SuccessResponse
import com.github.justalexandeer.simplenewsapp.data.network.ErrorResponse
import retrofit2.Response
import java.lang.Exception

class ConverterResponse {

    suspend fun <T> createCall(call: suspend () -> NetworkResponse<SuccessResponse, ErrorResponse>): Result<SuccessResponse> {
        val response = call.invoke()
        when(response) {
            is NetworkResponse.Success -> {
                Log.i(TAG, "createCall: Success")
                return Result.Success(response.body)
            }
            is NetworkResponse.ApiError -> {
                Log.i(TAG, "createCall: ApiError")
                val errorResponse: ErrorResponse = response.body as ErrorResponse
                Log.i(TAG, "createCall: ${errorResponse.message}")
                Log.i(TAG, "createCall: ${errorResponse.code}")
                Log.i(TAG, "createCall: ${errorResponse.status}")
                return Result.Error(Exception(errorResponse.message))
            }
            is NetworkResponse.NetworkError -> {
                Log.i(TAG, "createCall: NetworkError")
                return Result.Error(Exception(response.error))
            }
            is NetworkResponse.UnknownError -> {
                Log.i(TAG, "createCall: UnknownError")
                return Result.Error(Exception(response.error))
            }
        }
    }

    companion object {
        private const val TAG = "ConverterResponse"
    }

}