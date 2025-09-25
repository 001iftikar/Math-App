package com.example.mathapp.domain.repository

import com.example.mathapp.data.remote.SupabaseOperation
import com.example.mathapp.data.remote.model.GroupDto
import com.example.mathapp.data.remote.model.SharedGoalDto
import com.example.mathapp.domain.model.Group
import com.example.mathapp.domain.model.Message
import com.example.mathapp.domain.model.SharedGoal
import com.example.mathapp.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface SharedGoalRepository {
    fun createGroup(groupDto: GroupDto): Flow<SupabaseOperation<String>>

    fun getGroups(): Flow<SupabaseOperation<List<Group>>>
    fun joinGroup(groupId: String): Flow<SupabaseOperation<String>>
    fun getSpecificGroup(groupId: String): Flow<SupabaseOperation<Group>>
    fun getSharedGoalsForGroup(groupId: String): Flow<SupabaseOperation<List<SharedGoal>>>
    suspend fun createSharedGoal(sharedGoalDto: SharedGoalDto): SupabaseOperation<String>
    fun getGroupMembersForSpecificGroup(groupId: String): Flow<SupabaseOperation<List<UserProfile>>>

    fun isGroupAdmin(groupId: String): Flow<SupabaseOperation<Boolean>> // forced to use flow bcz of return mismatch from lambda
    suspend fun sendMessage(content: String, groupId: String): SupabaseOperation<Unit>
    fun getMessages(groupId: String): Flow<SupabaseOperation<List<Message>>>
    suspend fun getCurrentUserId(): String?
    suspend fun markAsCompleted(isCompleted: Boolean, sharedGoalId: String): SupabaseOperation<Boolean>
    suspend fun deleteGroup(groupId: String): SupabaseOperation<Unit>
    suspend fun leaveGroup(groupId: String): SupabaseOperation<Unit>
    suspend fun kickOutMember(groupId: String, userId: String): SupabaseOperation<Unit>
}