package com.github.justalexandeer.simplenewsapp.data.network

import com.github.justalexandeer.simplenewsapp.data.models.Source
import com.google.gson.annotations.SerializedName

data class ErrorResponse (
    @SerializedName("status") var status : String,
    @SerializedName("code") var code : String,
    @SerializedName("message") var message : String
)