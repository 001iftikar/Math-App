package com.example.mathapp.domain

import com.example.mathapp.data.remote.SupabaseOperation
import com.example.mathapp.domain.model.Group
import com.example.mathapp.domain.model.UserProfile
import com.example.mathapp.domain.repository.SharedGoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Had to define use case to hide supabase operation since it was giving problem while combining flow in viewmodel

class UseCases @Inject constructor(private val sharedGoalRepository: SharedGoalRepository) {

    fun getSpecificGroup(groupId: String): Flow<Result<Group>> =
        sharedGoalRepository.getSpecificGroup(groupId).map { it.toResult() }

    fun isGroupAdmin(groupId: String): Flow<Result<Boolean>> =
        sharedGoalRepository.isGroupAdmin(groupId).map { it.toResult() }

    fun getGroupMembersForSpecificGroup(groupId: String): Flow<Result<List<UserProfile>>> =
        sharedGoalRepository.getGroupMembersForSpecificGroup(groupId).map { it.toResult() }

    private fun <T> SupabaseOperation<T>.toResult(): Result<T> =
        when (this) {
            is SupabaseOperation.Failure -> Result.failure(exception)
            is SupabaseOperation.Success -> Result.success(data)
        }
}