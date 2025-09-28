package com.example.mathapp.di

import android.app.Application
import com.example.mathapp.utils.PreferenceDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Provides
    @Singleton
    fun providePreferenceDataStore(context: Application): PreferenceDataStore {
        return PreferenceDataStore(context)
    }
}