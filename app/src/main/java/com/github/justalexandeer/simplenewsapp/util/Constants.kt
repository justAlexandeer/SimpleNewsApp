package com.github.justalexandeer.simplenewsapp.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

enum class MainNewsTheme {
    COUNTRY, HEALTH, POLICY, FINANCE, BUSINESS, CULTURE, SPORT, AUTO, STYLE, SCIENCE;

    override fun toString(): String {
        return when(this) {
            COUNTRY -> "country"
            HEALTH -> "health"
            POLICY -> "policy"
            FINANCE -> "finance"
            BUSINESS -> "business"
            CULTURE -> "culture"
            SPORT -> "sport"
            AUTO -> "auto"
            STYLE -> "style"
            SCIENCE -> "science"
        }
    }
}

const val DEFAULT_IMAGE_URL = "https://i.ibb.co/QbJtRgK/newspaper-cover-page-260nw-142034005.jpg"