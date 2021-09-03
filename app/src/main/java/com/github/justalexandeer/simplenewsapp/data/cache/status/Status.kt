package com.github.justalexandeer.simplenewsapp.data.cache.status

sealed class Status<T> {
    data class Success<T>(val data: T): Status<T>()
    data class Error<T>(val data: T?, val message: String?): Status<T>()
    data class Loading<T>(val data: T?): Status<T>()
}