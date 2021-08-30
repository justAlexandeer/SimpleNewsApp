package com.github.justalexandeer.simplenewsapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_key")
data class RemoteKeys(
    @PrimaryKey
    val articleId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)
