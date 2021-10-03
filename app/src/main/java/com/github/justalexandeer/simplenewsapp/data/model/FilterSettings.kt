package com.github.justalexandeer.simplenewsapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterSettings (
    val dateFrom: String = "",
    val dateTo: String = "",
    val language: Language = Language.DEFAULT,
    val sortBy: SortBy = SortBy.PUBLISHEDAT
) : Parcelable

enum class Language {
    AR, DE, EN, ES, FR, HE, IT, NL, NO, PT, RU, SE, ZH, DEFAULT;
    override fun toString(): String {
        return when(this) {
            DEFAULT -> "All languages"
            AR -> "العربية"
            DE -> "Deutsch"
            EN -> "English"
            ES -> "Español"
            FR -> "français"
            HE -> "עברית"
            IT -> "Italiano"
            NL -> "Nederlands"
            NO -> "Norsk"
            PT -> "Português"
            RU -> "русский"
            SE -> "Davvisámegiella"
            ZH -> "中文"
        }
    }
    companion object {
        fun getValueByLanguageToString(language: String): Language {
            values().forEach {
                if (it.toString() == language)
                    return it
            }
            return DEFAULT
        }
    }
}

enum class SortBy {
    RELEVANCY, POPULARITY, PUBLISHEDAT;
    override fun toString(): String {
        return when(this) {
            RELEVANCY -> "relevancy"
            POPULARITY -> "popularity"
            PUBLISHEDAT -> "publishedAt"
        }
    }

    companion object {
        fun getValueBySortToString(sortBy: String): SortBy {
            SortBy.values().forEach {
                if (it.toString() == sortBy)
                    return it
            }
            return RELEVANCY
        }
    }
}