package com.example.mathapp.di

import com.example.mathapp.data.repository.AiRepositoryImpl
import com.example.mathapp.data.repository.BookRepositoryImpl
import com.example.mathapp.data.repository.PaperRepositoryImpl
import com.example.mathapp.data.repository.TeacherRepositoryImpl
import com.example.mathapp.domain.repository.AiRepository
import com.example.mathapp.domain.repository.BookRepository
import com.example.mathapp.domain.repository.PaperRepository
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

    @Singleton
    @Binds
    abstract fun bindBookRepository(impl: BookRepositoryImpl): BookRepository

    @Singleton
    @Binds
    abstract fun bindPaperRepository(impl: PaperRepositoryImpl): PaperRepository
    
    @Singleton
    @Binds
    abstract fun bindAiRepository(impl: AiRepositoryImpl): AiRepository
}