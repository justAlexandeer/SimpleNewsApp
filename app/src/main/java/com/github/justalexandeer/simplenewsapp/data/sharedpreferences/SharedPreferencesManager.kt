package com.github.justalexandeer.simplenewsapp.data.sharedpreferences

import android.content.Context
import android.util.Log
import com.github.justalexandeer.simplenewsapp.R
import com.github.justalexandeer.simplenewsapp.util.MainNewsTheme
import javax.inject.Inject


class SharedPreferencesManager @Inject constructor(
    private val appContext: Context
) {
    private val sharedPreferences =
        appContext.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun firstStart(
        key: String
    ) {
        editor.putBoolean(key, false)
        editor.apply()
    }

    fun isFirstStart(
        key: String
    ): Boolean {
        return sharedPreferences.getBoolean(key, true)
    }

    fun getSelectedTheme(
        key: String
    ): Set<MainNewsTheme> {
        val mainNewsTheme = mutableSetOf<MainNewsTheme>()
        sharedPreferences.getStringSet(key, null)?.forEach {
            when(it) {
                "country" -> mainNewsTheme.add(MainNewsTheme.COUNTRY)
                "finance" -> mainNewsTheme.add(MainNewsTheme.FINANCE)
                "health" -> mainNewsTheme.add(MainNewsTheme.HEALTH)
                "policy" -> mainNewsTheme.add(MainNewsTheme.POLICY)
                "business" -> mainNewsTheme.add(MainNewsTheme.BUSINESS)
                "culture" -> mainNewsTheme.add(MainNewsTheme.CULTURE)
                "sport" -> mainNewsTheme.add(MainNewsTheme.SPORT)
                "auto" -> mainNewsTheme.add(MainNewsTheme.AUTO)
                "style" -> mainNewsTheme.add(MainNewsTheme.STYLE)
                "science" -> mainNewsTheme.add(MainNewsTheme.SCIENCE)
            }
        }
        return mainNewsTheme
    }

    fun setSelectedTheme(
        key: String,
        value: Set<String>
    ) {
        editor.putStringSet(key, value)
        editor.apply()
    }

    fun getDefaultTheme() = setOf(
        MainNewsTheme.COUNTRY.toString(),
        MainNewsTheme.FINANCE.toString(),
        MainNewsTheme.HEALTH.toString(),
        MainNewsTheme.POLICY.toString()
    )

    companion object {
        private const val TAG = "SharedPreferencesManage"
        const val APP_PREFERENCES = "AppPreferences"
        const val SELECTED_THEMES = "Themes"
        const val IS_FIRST_START = "Start"
    }

}