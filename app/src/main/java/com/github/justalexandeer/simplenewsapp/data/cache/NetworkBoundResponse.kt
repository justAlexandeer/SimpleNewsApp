package com.github.justalexandeer.simplenewsapp.data.cache

import com.github.justalexandeer.simplenewsapp.data.cache.status.Status
import com.github.justalexandeer.simplenewsapp.ui.fragment.newsmain.MainContract
import com.github.justalexandeer.simplenewsapp.data.network.response.Result
import com.github.justalexandeer.simplenewsapp.data.network.response.SuccessResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

abstract class NetworkBoundResponse<ResultType, RequestType> {

    fun asFlow() = flow {
        emit(Status.Loading(null))

        val dataFromDb = loadFromDb().first()
        if (shodFetch(dataFromDb)) {
            val dataFromNetwork = loadFromNetwork()
            when (dataFromNetwork) {
                is Result.Success<*> -> {
                    val mapDate = mapData(dataFromNetwork)
                    emit(Status.Success(mapDate))
                    saveToDb(mapDate)
                }
                is Result.Error -> {
                    emit(Status.Error(null, dataFromNetwork.error.message))
                }
            }
        }

    }

    abstract suspend fun loadFromNetwork(): RequestType
    abstract suspend fun loadFromDb(): Flow<ResultType>
    abstract fun shodFetch(data: ResultType?): Boolean
    abstract suspend fun saveToDb(data: ResultType)
    abstract fun mapData(result: RequestType): ResultType
}