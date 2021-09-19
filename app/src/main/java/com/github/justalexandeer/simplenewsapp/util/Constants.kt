package com.github.justalexandeer.simplenewsapp.util

enum class MainNewsTheme {
    COUNTRY, HEALTH, POLICY, FINANCE;

    override fun toString(): String {
        return when(this) {
            COUNTRY -> "country"
            HEALTH -> "health"
            POLICY -> "policy"
            FINANCE -> "finance"
        }
    }
}

const val COUNT_NEWS_IN_CARD_THEME = 2