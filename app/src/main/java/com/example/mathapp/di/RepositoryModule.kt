package com.example.mathapp.di

import com.example.mathapp.data.repository.BookRepositoryImpl
import com.example.mathapp.data.repository.PaperRepositoryImpl
import com.example.mathapp.data.repository.SessionRepositoryImpl
import com.example.mathapp.data.repository.SubjectRepositoryImpl
import com.example.mathapp.data.repository.SupabaseRepositoryImpl
import com.example.mathapp.data.repository.TaskRepositoryImpl
import com.example.mathapp.data.repository.TeacherRepositoryImpl
import com.example.mathapp.domain.repository.BookRepository
import com.example.mathapp.domain.repository.PaperRepository
import com.example.mathapp.domain.repository.SessionRepository
import com.example.mathapp.domain.repository.SubjectRepository
import com.example.mathapp.domain.repository.SupabaseRepository
import com.example.mathapp.domain.repository.TaskRepository
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
    abstract fun bindSubjectRepository(impl: SubjectRepositoryImpl): SubjectRepository

    @Singleton
    @Binds
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Singleton
    @Binds
    abstract fun bindSessionRepository(impl: SessionRepositoryImpl): SessionRepository

    @Singleton
    @Binds
    abstract fun bindSupabaseRepository(impl: SupabaseRepositoryImpl): SupabaseRepository
}