package com.example.mathapp.domain.repository

import com.example.mathapp.data.SupabaseOperation
import com.example.mathapp.domain.model.SupabaseUser
import kotlinx.coroutines.flow.Flow

interface SupabaseRepository {
    fun signUp(emailValue: String, passwordValue: String, name: String): Flow<SupabaseOperation<SupabaseUser>>

    fun signIn(emailValue: String, passwordValue: String): Flow<SupabaseOperation<SupabaseUser>>

    suspend fun checkUserLogin(): Boolean
}