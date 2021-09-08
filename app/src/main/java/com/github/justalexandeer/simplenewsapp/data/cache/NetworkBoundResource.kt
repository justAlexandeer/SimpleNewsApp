package com.github.justalexandeer.simplenewsapp.data.cache

import android.app.DownloadManager
import android.util.Log
import com.github.justalexandeer.simplenewsapp.data.cache.status.Status
import com.github.justalexandeer.simplenewsapp.data.network.response.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

abstract class NetworkBoundResource<ResultType, RequestType>(val query: String) {

    fun asFlow() = flow {
        val dataFromDb = loadFromDb(TYPE_ARTICLE, query).first()
        if (shodFetch(dataFromDb)) {
            emit(Status.Loading(dataFromDb))
            val dataFromNetwork = loadFromNetwork(query)
            when (dataFromNetwork) {
                is Result.Success<*> -> {
                    val mapDate = mapData(dataFromNetwork, query)
                    deleteOldArticle(TYPE_ARTICLE, query)
                    saveToDb(mapDate)
                    emit(Status.Success(loadFromDb(TYPE_ARTICLE, query).first()))
                }
                is Result.Error -> {
                    emit(Status.Error(null, dataFromNetwork.error.message))
                }
            }
        }
    }

    abstract suspend fun loadFromNetwork(query: String): RequestType
    abstract suspend fun loadFromDb(articleType: String, query: String): Flow<ResultType>
    abstract fun shodFetch(data: ResultType?): Boolean
    abstract suspend fun saveToDb(data: ResultType)
    abstract fun mapData(result: RequestType, query: String): ResultType
    abstract suspend fun deleteOldArticle(articleType: String, query: String)

    companion object {
        const val TYPE_ARTICLE = "Main"
        const val NETWORK_PAGE_SIZE = 3
    }
}