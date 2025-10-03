package com.iftikar.mathapp.domain.repository

import com.iftikar.mathapp.data.remote.SupabaseOperation
import com.iftikar.mathapp.data.remote.model.GoalRequestDto
import com.iftikar.mathapp.domain.model.GoalModel
import kotlinx.coroutines.flow.Flow

interface UserGoalRepository {
    suspend fun upsertGoal(goalRequestDto: GoalRequestDto): Flow<SupabaseOperation<String>>
    fun getAllGoals(): Flow<SupabaseOperation<List<GoalModel>>>

    fun getSpecificGoal(goalId: String): Flow<SupabaseOperation<GoalModel>>

    suspend fun deleteFinishedGoal(goalId: String): Flow<SupabaseOperation<String>>

}