package com.example.mathapp.di

import com.example.mathapp.data.repository.TeacherRepositoryImpl
import com.example.mathapp.domain.repository.TeacherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindTeacherRepository(impl: TeacherRepositoryImpl): TeacherRepository
}