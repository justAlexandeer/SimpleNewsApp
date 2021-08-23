package com.github.justalexandeer.simplenewsapp.data.models

import com.google.gson.annotations.SerializedName

data class SuccessResponse (
    @SerializedName("status") var status : String,
    @SerializedName("totalResults") var totalResults : Int,
    @SerializedName("articles") var articles : List<Articles>
)