package com.github.justalexandeer.simplenewsapp.ui.account.favorite

import androidx.lifecycle.ViewModel
import com.github.justalexandeer.simplenewsapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@HiltViewModel
class FavoriteNewsViewModel @Inject constructor(
    val mainRepository: MainRepository
) : ViewModel() {

    val listFavoriteNews = MutableStateFlow(mainRepository.getFavoriteNews())


}