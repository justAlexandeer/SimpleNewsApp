package com.github.justalexandeer.simplenewsapp.data.cache

import com.github.justalexandeer.simplenewsapp.data.cache.status.Status
import com.github.justalexandeer.simplenewsapp.data.network.response.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

abstract class NetworkBoundResource<ResultType, RequestType> {

    fun asFlow() = flow {
        val dataFromDb = loadFromDb().first()
        if (shodFetch(dataFromDb)) {
            emit(Status.Loading(null))
            val dataFromNetwork = loadFromNetwork()
            when (dataFromNetwork) {
                is Result.Success<*> -> {
                    val mapDate = mapData(dataFromNetwork)
                    deleteOldArticle(TYPE_ARTICLE)
                    saveToDb(mapDate)
                    emit(Status.Success(loadFromDb().first()))
                }
                is Result.Error -> {
                    emit(Status.Error(dataFromDb, dataFromNetwork.error.message))
                }
            }
        }
    }

    abstract suspend fun loadFromNetwork(): RequestType
    abstract suspend fun loadFromDb(): Flow<ResultType>
    abstract fun shodFetch(data: ResultType?): Boolean
    abstract suspend fun saveToDb(data: ResultType)
    abstract fun mapData(result: RequestType): ResultType
    abstract suspend fun deleteOldArticle(articleType: String)

    companion object {
        const val TYPE_ARTICLE = "Main"
    }
}