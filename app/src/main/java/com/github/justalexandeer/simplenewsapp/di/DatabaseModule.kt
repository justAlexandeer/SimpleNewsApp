package com.github.justalexandeer.simplenewsapp.di

import android.content.Context
import androidx.room.Room
import com.github.justalexandeer.simplenewsapp.data.db.AppDatabase
import com.github.justalexandeer.simplenewsapp.data.db.ArticleDao
import com.github.justalexandeer.simplenewsapp.data.db.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideArticleDao(database: AppDatabase): ArticleDao {
        return database.articleDao()
    }

    @Provides
    fun provideRemoteKey(database: AppDatabase): RemoteKeyDao {
        return database.remoteKeyDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "AppDatabase"
        ).build()
    }


}