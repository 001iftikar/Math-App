package com.example.mathapp.di

import android.app.Application
import androidx.room.Room
import com.example.mathapp.data.local.AppLocalDatabase
import com.example.mathapp.data.local.SessionDao
import com.example.mathapp.data.local.SubjectDao
import com.example.mathapp.data.local.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(application: Application): AppLocalDatabase {
        return Room
            .databaseBuilder(
                application,
                AppLocalDatabase::class.java,
                "studysmart.db"
            ).build()
    }

    @Provides
    @Singleton
    fun provideSubjectDao(database: AppLocalDatabase): SubjectDao {
        return database.subjectDao()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: AppLocalDatabase): TaskDao = database.taskDao()

    @Provides
    @Singleton
    fun provideSessionDao(database: AppLocalDatabase) = database.sessionDao()
}