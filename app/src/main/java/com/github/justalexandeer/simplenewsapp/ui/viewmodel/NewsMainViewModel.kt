package com.github.justalexandeer.simplenewsapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.justalexandeer.simplenewsapp.data.network.response.Article
import com.github.justalexandeer.simplenewsapp.data.network.response.SuccessResponse
import com.github.justalexandeer.simplenewsapp.repository.MainRepository
import com.github.justalexandeer.simplenewsapp.data.network.response.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import com.github.justalexandeer.simplenewsapp.data.network.response.Result.Error as Error

@HiltViewModel
class NewsMainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    val mutableListNewsBitcoin = MutableLiveData<List<Article>>()
    init {
        Log.i(TAG, "init: ")
    }

    fun getNews() {
        // Мб тут темы новостей выбираются


        // Изменить тут все на кеширование (возвращаемый тип данных)
        viewModelScope.launch {


            mainRepository.getNews("bitcoin").collect {
                when(it) {
                    is Result.Success -> {
                        mutableListNewsBitcoin.value = it.data.articles
                        Log.i(TAG, "getNews: success")
                    }
                    is Error -> {
                        Log.i(TAG, "getNews: ${it.error}")
                    }
                }
            }

        }

    }


    companion object {
        private const val TAG = "NewsMainViewModel"
    }
}