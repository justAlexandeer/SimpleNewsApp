package com.github.justalexandeer.simplenewsapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import com.github.justalexandeer.simplenewsapp.data.db.entity.RemoteKeys

@Database(
    entities = [ArticleDb::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}