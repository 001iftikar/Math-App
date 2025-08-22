package com.example.mathapp.data.repository

import android.util.Log
import com.example.mathapp.data.SupabaseOperation
import com.example.mathapp.data.remote.GoalRequestDto
import com.example.mathapp.domain.repository.UserGoalRepository
import com.example.mathapp.utils.SupabaseConstants
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserGoalRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : UserGoalRepository {
    override suspend fun insertGoal(goalRequestDto: GoalRequestDto): Flow<SupabaseOperation<String>> =
        flow {
            try {
                val userId = supabaseClient.auth.currentUserOrNull()?.id
                if (userId != null) {
                    val goal = goalRequestDto.copy(userId = userId)
                    supabaseClient.postgrest[SupabaseConstants.GOAL_TABLE].insert(
                        goal
                    )
                }
                emit(SupabaseOperation.Success("Goal created!!"))
            } catch (e: Exception) {
                Log.e("Goal-Error", "insertGoal: ${e.localizedMessage}")
            }
        }
}