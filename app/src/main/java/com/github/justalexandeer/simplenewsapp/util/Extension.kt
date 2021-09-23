package com.github.justalexandeer.simplenewsapp.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

fun FragmentActivity.hideKeyboard() {
    this.currentFocus?.apply {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(windowToken, 0)
    }
}