package com.github.justalexandeer.simplenewsapp.di

import android.content.Context
import com.github.justalexandeer.simplenewsapp.data.SharedPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataSetModule {

    @Provides
    @Singleton
    fun provideDataSetManager(@ApplicationContext appContext: Context): SharedPreferencesManager {
        return SharedPreferencesManager(appContext)
    }

}