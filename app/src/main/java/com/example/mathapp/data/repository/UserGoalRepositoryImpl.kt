package com.example.mathapp.data.repository

import android.util.Log
import com.example.mathapp.data.remote.SupabaseOperation
import com.example.mathapp.data.remote.model.GoalRequestDto
import com.example.mathapp.data.remote.model.GoalResponseDto
import com.example.mathapp.domain.model.GoalModel
import com.example.mathapp.domain.repository.UserGoalRepository
import com.example.mathapp.mappers.toGoalModel
import com.example.mathapp.utils.SupabaseConstants
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
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

    override fun getAllGoals(): Flow<SupabaseOperation<List<GoalModel>>> = flow {
        try {
            val userId = supabaseClient.auth.currentUserOrNull()?.id
           if (userId != null) {
               val goals = supabaseClient.postgrest[SupabaseConstants.GOAL_TABLE]
                   .select {
                       filter {
                           eq("user_id", userId) // RLS enabled, but just to be safe
                       }
                   }
                   .decodeList<GoalResponseDto>()
                   .map {
                       it.toGoalModel()
                   }

               emit(SupabaseOperation.Success(
                   data = goals
               ))
           } else {
               Log.e("Goal-Repo-all", "getAllGoals: User null")
               emit(SupabaseOperation.Failure(
                   exception = NullPointerException("User not logged in!")
               ))
           }
        } catch (e: HttpRequestException) {
            Log.e("Goal-Repo-Http", "getAllGoals: ${e.localizedMessage}")
            emit(SupabaseOperation.Failure(Exception("Please check your internet connection!")))
        } catch (e: IOException) {
            Log.e("Goal-Repo-IO", "getAllGoals: ${e.localizedMessage}")
            emit(SupabaseOperation.Failure(Exception("Could not retrieve Goals, check if you are online")))
        } catch (e: Exception) {
            Log.e("Goal-Repo-all", "getAllGoals: ${e.localizedMessage}")
            emit(SupabaseOperation.Failure(Exception("Error occurred!")))
        }
    }
}