package com.github.justalexandeer.simplenewsapp.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun dateConverter(dateNews: String): String {
    val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(dateNews)

    val calendar = Calendar.getInstance()
    calendar.time = date
    val timeInMillis = calendar.timeInMillis

    val requiredDateFormat = SimpleDateFormat("dd.MM.yyyy")
    return requiredDateFormat.format(timeInMillis).toString()
}