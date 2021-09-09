package com.github.justalexandeer.simplenewsapp.ui.walkthrough

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.data.SharedPreferencesManager
import com.github.justalexandeer.simplenewsapp.data.SharedPreferencesManager.Companion.SELECTED_THEMES
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class WalkthroughViewModel @Inject constructor(
    val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    private val _isFirstStart = MutableStateFlow(false)
    val isFirstStart = _isFirstStart
    private val _isNeedStartNewActivity = MutableStateFlow(false)
    val isNeedStartNewActivity = _isNeedStartNewActivity
    private val _isDefaultThemeSet = MutableStateFlow(false)
    val isDefaultThemeSet = _isDefaultThemeSet
    private val _isButtonClick = MutableStateFlow(false)
    val isButtonClick = _isButtonClick

    init {
        Log.i(TAG, "init")
    }

    fun checkIsFirstStart() {
        if (sharedPreferencesManager.isFirstStart(SharedPreferencesManager.IS_FIRST_START)) {
            sharedPreferencesManager.firstStart(SharedPreferencesManager.IS_FIRST_START)
            setupDefaultTheme()
        } else {
            _isNeedStartNewActivity.value = true
        }
    }

    private fun setupDefaultTheme() {
        sharedPreferencesManager.setSelectedTheme(
            SELECTED_THEMES,
            sharedPreferencesManager.getDefaultTheme()
        )
        isDefaultThemeSet.value = true
    }

    companion object {
        private const val TAG = "WalkthroughViewModel"
    }

}