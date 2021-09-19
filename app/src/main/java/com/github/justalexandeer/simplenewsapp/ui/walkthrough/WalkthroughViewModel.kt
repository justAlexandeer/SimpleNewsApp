package com.github.justalexandeer.simplenewsapp.ui.walkthrough

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.justalexandeer.simplenewsapp.data.sharedpreferences.SharedPreferencesManager
import com.github.justalexandeer.simplenewsapp.data.sharedpreferences.SharedPreferencesManager.Companion.SELECTED_THEMES
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalkthroughViewModel @Inject constructor(
    val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    private val _isFirstStart = MutableStateFlow(false)
    val isFirstStart = _isFirstStart as StateFlow<Boolean>
    private val _isNeedStartNewActivity = MutableStateFlow(false)
    val isNeedStartNewActivity  = _isNeedStartNewActivity as StateFlow<Boolean>
    private val _isDefaultThemeSet = MutableStateFlow(false)
    val isDefaultThemeSet = _isDefaultThemeSet as StateFlow<Boolean>
    private val _isButtonClick = MutableStateFlow(false)
    val isButtonClick = _isButtonClick as StateFlow<Boolean>

    fun checkIsFirstStart() {
        if (sharedPreferencesManager.isFirstStart(SharedPreferencesManager.IS_FIRST_START)) {
            setupDefaultTheme()
        } else {
            _isNeedStartNewActivity.value = true
        }
    }

    fun onButtonClick() {
        _isButtonClick.value = true
        sharedPreferencesManager.firstStart(SharedPreferencesManager.IS_FIRST_START)
    }

    private fun setupDefaultTheme() {
        sharedPreferencesManager.setSelectedTheme(
            SELECTED_THEMES,
            sharedPreferencesManager.getDefaultTheme()
        )
        _isDefaultThemeSet.value = true
    }

    companion object {
        private const val TAG = "WalkthroughViewModel"
    }

}