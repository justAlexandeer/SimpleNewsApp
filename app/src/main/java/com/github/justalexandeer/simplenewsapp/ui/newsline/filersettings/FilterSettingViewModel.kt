package com.github.justalexandeer.simplenewsapp.ui.newsline.filersettings

import androidx.lifecycle.ViewModel
import com.github.justalexandeer.simplenewsapp.data.model.FilterSettings

class FilterSettingViewModel: ViewModel() {
    var currentFilterSetting: FilterSettings? = null


    fun setupFilterSetting(filterSetting: FilterSettings) {
        if (currentFilterSetting == null)
            currentFilterSetting = filterSetting
    }
}