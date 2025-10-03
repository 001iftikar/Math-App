package com.iftikar.mathapp.di

import android.app.Application
import androidx.room.Room
import com.iftikar.mathapp.data.local.AppLocalDatabase
import com.iftikar.mathapp.data.local.SubjectDao
import com.iftikar.mathapp.data.local.TaskDao
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