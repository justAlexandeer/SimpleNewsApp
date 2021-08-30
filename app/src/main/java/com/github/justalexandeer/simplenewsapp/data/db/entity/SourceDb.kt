package com.github.justalexandeer.simplenewsapp.data.db.entity

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class SourceDb(
    @PrimaryKey
    @field:SerializedName("id") var id : Long,
    @field:SerializedName("name") var name : String
)
