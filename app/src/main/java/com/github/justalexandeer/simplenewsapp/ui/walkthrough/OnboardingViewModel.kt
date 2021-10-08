package com.github.justalexandeer.simplenewsapp.ui.walkthrough

import androidx.lifecycle.ViewModel
import com.github.justalexandeer.simplenewsapp.data.sharedpreferences.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    val sharedPreferencesManager: SharedPreferencesManager
): ViewModel() {

    val onNextButtonClick: Channel<Boolean> = Channel ()
    val isDefaultThemeSet = MutableStateFlow(false)
    val isButtonStartClick = MutableStateFlow(false)
    val isNeedStartOnboarding = MutableStateFlow(true)

    fun checkIsFirstStart() {
        if (sharedPreferencesManager.isFirstStart(SharedPreferencesManager.IS_FIRST_START)) {
            setupDefaultTheme()
        } else {
            isNeedStartOnboarding.value = false
        }
    }

    fun changeValueOfFirstStart() {
        sharedPreferencesManager.firstStart(SharedPreferencesManager.IS_FIRST_START)
    }

    private fun setupDefaultTheme() {
        sharedPreferencesManager.setSelectedTheme(
            SharedPreferencesManager.SELECTED_THEMES,
            sharedPreferencesManager.getDefaultTheme()
        )
        isDefaultThemeSet.value = true
    }
}