package com.github.justalexandeer.simplenewsapp.ui.account

import androidx.lifecycle.ViewModel
import com.github.justalexandeer.simplenewsapp.data.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {


    companion object {
        private const val TAG = "AccountViewModel"
    }

}