package com.github.justalexandeer.simplenewsapp.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import com.google.gson.annotations.SerializedName

@Entity(tableName = "article")
data class ArticleDb(
    @field:SerializedName("author") var author : String,
    @field:SerializedName("title") var title : String,
    @field:SerializedName("description") var description : String,
    @field:SerializedName("url") var url : String,
    @field:SerializedName("urlToImage") var urlToImage : String,
    @field:SerializedName("publishedAt") var publishedAt : String,
    @field:SerializedName("content") var content : String,
    @field:SerializedName("query") var query: String,
    @field:SerializedName("type") var type: String,
    @PrimaryKey(autoGenerate = true)
    @field:SerializedName("idArticle") val idArticle: Long = 0L
)

