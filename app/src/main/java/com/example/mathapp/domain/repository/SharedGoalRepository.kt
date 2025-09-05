package com.example.mathapp.domain.repository

import com.example.mathapp.data.remote.SupabaseOperation
import com.example.mathapp.data.remote.model.GroupDto
import com.example.mathapp.domain.model.Group
import kotlinx.coroutines.flow.Flow

interface SharedGoalRepository {
    fun createGroup(groupDto: GroupDto): Flow<SupabaseOperation<String>>

    fun getGroups(): Flow<SupabaseOperation<List<Group>>>
}