package com.github.justalexandeer.simplenewsapp.data.network.response

import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("source") var source : Source,
    @SerializedName("author") var author : String?,
    @SerializedName("title") var title : String,
    @SerializedName("description") var description : String,
    @SerializedName("url") var url : String,
    @SerializedName("urlToImage") var urlToImage : String?,
    @SerializedName("publishedAt") var publishedAt : String,
    @SerializedName("content") var content : String
)
