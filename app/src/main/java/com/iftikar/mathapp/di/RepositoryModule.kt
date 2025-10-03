package com.iftikar.mathapp.di

import com.iftikar.mathapp.data.repository.BookRepositoryImpl
import com.iftikar.mathapp.data.repository.PaperRepositoryImpl
import com.iftikar.mathapp.data.repository.SessionRepositoryImpl
import com.iftikar.mathapp.data.repository.SharedGoalRepositoryImpl
import com.iftikar.mathapp.data.repository.SubjectRepositoryImpl
import com.iftikar.mathapp.data.repository.SupabaseAuthRepositoryImpl
import com.iftikar.mathapp.data.repository.TaskRepositoryImpl
import com.iftikar.mathapp.data.repository.TeacherRepositoryImpl
import com.iftikar.mathapp.data.repository.UserGoalRepositoryImpl
import com.iftikar.mathapp.domain.repository.BookRepository
import com.iftikar.mathapp.domain.repository.PaperRepository
import com.iftikar.mathapp.domain.repository.SessionRepository
import com.iftikar.mathapp.domain.repository.SharedGoalRepository
import com.iftikar.mathapp.domain.repository.SubjectRepository
import com.iftikar.mathapp.domain.repository.SupabaseAuthRepository
import com.iftikar.mathapp.domain.repository.TaskRepository
import com.iftikar.mathapp.domain.repository.TeacherRepository
import com.iftikar.mathapp.domain.repository.UserGoalRepository
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
    abstract fun bindSupabaseRepository(impl: SupabaseAuthRepositoryImpl): SupabaseAuthRepository

    @Singleton
    @Binds
    abstract fun bindUserGoalRepository(impl: UserGoalRepositoryImpl): UserGoalRepository

    @Singleton
    @Binds
    abstract fun bindSharedGoalRepository(impl: SharedGoalRepositoryImpl): SharedGoalRepository
}