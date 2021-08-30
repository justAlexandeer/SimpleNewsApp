package com.github.justalexandeer.simplenewsapp.data.network.response

import com.google.gson.annotations.SerializedName

data class SuccessResponse (
    @SerializedName("status") var status : String,
    @SerializedName("totalResults") var totalResults : Int,
    @SerializedName("articles") var articles : List<Article>
)