package com.github.justalexandeer.simplenewsapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.github.justalexandeer.simplenewsapp.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class SharedPreferencesManager @Inject constructor(
    private val appContext: Context
) {
    private val sharedPreferences = appContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun firstStart(
        key: String
    ) {
        editor.putBoolean(key, true)
        editor.apply()
    }

    fun isFirstStart(
        key: String
    ) : Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getSelectedTheme(
        key: String
    ) : Set<String>? {
        return sharedPreferences.getStringSet(key, null)
    }

    fun setSelectedTheme(
        key: String,
        value: Set<String?>
    ){
        editor.putStringSet(key, value)
        editor.apply()
    }

    fun getDefaultTheme()  = appContext.resources.getStringArray(R.array.all_theme_main_line).toSet()

    companion object {
        const val APP_PREFERENCES = "AppPreferences"
        const val SELECTED_THEMES = "Themes"
        const val IS_FIRST_START = "Start"
    }

}