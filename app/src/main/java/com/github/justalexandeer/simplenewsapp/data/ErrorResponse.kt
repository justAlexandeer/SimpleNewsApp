package com.github.justalexandeer.simplenewsapp.data

import com.github.justalexandeer.simplenewsapp.data.models.Source
import com.google.gson.annotations.SerializedName

data class ErrorResponse (
    @SerializedName("status") var status : Source,
    @SerializedName("code") var code : String,
    @SerializedName("message") var message : String
)