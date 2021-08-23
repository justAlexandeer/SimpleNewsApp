package com.github.justalexandeer.simplenewsapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.justalexandeer.simplenewsapp.api.NetworkResponse
import com.github.justalexandeer.simplenewsapp.api.NewsApi
import com.github.justalexandeer.simplenewsapp.data.models.Articles
import com.github.justalexandeer.simplenewsapp.data.models.SuccessResponse
import com.github.justalexandeer.simplenewsapp.repository.MainRepository
import retrofit2.HttpException
import java.io.IOException
import javax.security.auth.login.LoginException

class NewsPagingSource(
    private val newsApi: NewsApi
): PagingSource<Int, Articles>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Articles> {
        val position = params.key ?: NEWS_STARTING_PAGE_INDEX
        return try {
            val response = newsApi.getGetAllNews("bitcoin", MainRepository.apiKey, NETWORK_PAGE_SIZE, position)
            val articles = response.articles
            Log.i("NewsNetworkViewModel", "load: ${articles.size}")
            val nextKey = if (articles.isEmpty()) {
                null
            } else {
                position + 1
            }
            LoadResult.Page(
                data = articles,
                prevKey = if (position == NEWS_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            Log.i(TAG, "load: IOExcpetion")
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Log.i(TAG, "load: ${exception.code()}")
            Log.i(TAG, "load: ${exception.message()}")
            Log.i(TAG, "load: ${exception.response()}")
            Log.i(TAG, "load: -------------------------")
            Log.i(TAG, "load: ${exception.cause}")
            Log.i(TAG, "load: ${exception.message}")
            Log.i(TAG, "load: ${exception.stackTrace}")
            Log.i(TAG, "load: ${exception.toString()}")
            Log.i(TAG, "load: HttpException")
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Articles>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
    companion object {
        private const val NEWS_STARTING_PAGE_INDEX = 1
        const val NETWORK_PAGE_SIZE = 60
        private const val TAG = "NewsPagingSource"
    }
}