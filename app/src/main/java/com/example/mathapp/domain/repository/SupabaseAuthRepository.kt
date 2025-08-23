package com.example.mathapp.domain.repository

import com.example.mathapp.data.remote.SupabaseOperation
import com.example.mathapp.domain.model.SupabaseUser
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.Flow

interface SupabaseAuthRepository {
    fun signUp(emailValue: String, passwordValue: String, name: String): Flow<SupabaseOperation<SupabaseUser>>

    fun signIn(emailValue: String, passwordValue: String): Flow<SupabaseOperation<SupabaseUser>>

    fun loadUserSession(): Flow<SupabaseOperation<UserSession>>
}