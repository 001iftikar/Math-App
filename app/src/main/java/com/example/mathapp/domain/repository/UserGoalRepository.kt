package com.example.mathapp.domain.repository

import com.example.mathapp.data.remote.SupabaseOperation
import com.example.mathapp.data.remote.model.GoalRequestDto
import com.example.mathapp.domain.model.GoalModel
import kotlinx.coroutines.flow.Flow

interface UserGoalRepository {
    suspend fun insertGoal(goalRequestDto: GoalRequestDto): Flow<SupabaseOperation<String>>
    fun getAllGoals(): Flow<SupabaseOperation<List<GoalModel>>>

}