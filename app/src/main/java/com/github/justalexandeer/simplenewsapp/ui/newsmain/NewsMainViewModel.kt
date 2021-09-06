package com.github.justalexandeer.simplenewsapp.ui.newsmain

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.justalexandeer.simplenewsapp.data.network.response.Article
import com.github.justalexandeer.simplenewsapp.repository.MainRepository
import com.github.justalexandeer.simplenewsapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsMainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel<MainContractNewsMain.Event, MainContractNewsMain.State, MainContractNewsMain.Effect>() {

    override fun createInitialState(): MainContractNewsMain.State {
        return MainContractNewsMain.State(MainContractNewsMain.MainNewsState.Idle)
    }

    override fun handleEvent(event: MainContractNewsMain.Event) {
        when(event) {
            is MainContractNewsMain.Event.GetMainNews -> {
                getNews()
            }
        }
    }

    fun getNews() {
        viewModelScope.launch {
            mainRepository.getMainNews().collect {
                setState {
                    copy(mainNewsState = it)
                }
            }
        }
    }


    companion object {
        private const val TAG = "NewsMainViewModel"
    }
}