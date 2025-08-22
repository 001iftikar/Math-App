package com.example.mathapp.domain.repository

import com.example.mathapp.data.SupabaseOperation
import com.example.mathapp.data.remote.GoalRequestDto
import kotlinx.coroutines.flow.Flow

interface UserGoalRepository {
    suspend fun insertGoal(goalRequestDto: GoalRequestDto): Flow<SupabaseOperation<String>>
}