package com.example.mathapp.data.repository

import android.util.Log
import com.example.mathapp.data.remote.SupabaseOperation
import com.example.mathapp.data.remote.model.GroupDto
import com.example.mathapp.data.remote.model.GroupMemberDto
import com.example.mathapp.data.remote.model.SharedGoalDto
import com.example.mathapp.domain.model.Group
import com.example.mathapp.domain.model.SharedGoal
import com.example.mathapp.domain.model.UserProfile
import com.example.mathapp.domain.repository.SharedGoalRepository
import com.example.mathapp.mappers.toGroup
import com.example.mathapp.mappers.toSharedGoal
import com.example.mathapp.utils.SupabaseConstants
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.io.IOException
import java.util.UUID
import javax.inject.Inject

class SharedGoalRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : SharedGoalRepository {
    override fun createGroup(groupDto: GroupDto): Flow<SupabaseOperation<String>> = flow {
        try {
            val groupId = UUID.randomUUID().toString()
            val userId = supabaseClient.auth.currentUserOrNull()?.id
            if (userId != null) {
                val group = groupDto.copy(id = groupId, admin = userId)
                val member = GroupMemberDto(
                    group_id = groupId,
                    user_id = userId,
                    role = "admin"
                )

                supabaseClient.postgrest[SupabaseConstants.GROUP_TABLE]
                    .upsert(group)

                supabaseClient.postgrest[SupabaseConstants.GROUP_MEMBER_TABLE]
                    .upsert(member)
                emit(SupabaseOperation.Success("Group created"))
            }
        } catch (e: IOException) {
            emit(SupabaseOperation.Failure(Exception("Network error!")))
        } catch (e: Exception) {
            Log.e("Shared-Goal-Repo", "createGroup: $e")
            emit(SupabaseOperation.Failure(Exception("Group creation failed!")))
        }
    }

    override fun getGroups(): Flow<SupabaseOperation<List<Group>>> = flow {
        try {
            val userId = supabaseClient.auth.currentUserOrNull()?.id
            if (userId != null) {
                var filteredGroups: List<Group>
                getGroupMembers { members ->
                    val belongedColumn =
                        members.filter { it.user_id == userId } // only return the columns userId is in
                    val groupIds: List<String> = belongedColumn
                        .map {
                            it.group_id
                        } // store the group ids in the list to query later in the group table to get the groups where this user appears
                    if (groupIds.isNotEmpty()) { // only run if the group id list is not empty, otherwise it returns the whole db if it does not fine any matches
                        val userProfiles = supabaseClient.postgrest["profiles"]
                            .select()
                            .decodeList<UserProfile>()
                        val profileMap =
                            userProfiles.associateBy { it.id } // only query the db once, then store it in a hashmap to avoid filtering each time inside map
                        filteredGroups = supabaseClient.postgrest[SupabaseConstants.GROUP_TABLE]
                            .select {
                                filter {
                                    or {
                                        groupIds.forEach {
                                            eq("id", it)
                                        }
                                    }
                                }
                            }
                            .decodeList<GroupDto>()
                            .map { groupDto ->
                                val adminName = profileMap[groupDto.admin]?.name ?: "Unknown"
                                groupDto.toGroup(adminName)
                            }
                        emit(SupabaseOperation.Success(filteredGroups))
                    } else {
                        emit(SupabaseOperation.Success(emptyList<Group>()))
                    }
                }
            }
        } catch (e: IOException) {
            emit(SupabaseOperation.Failure(Exception("Please check internet connection")))
        } catch (e: Exception) {
            emit(SupabaseOperation.Failure(Exception("Some error happened")))
        }
    }

    override fun joinGroup(groupId: String): Flow<SupabaseOperation<String>> = flow {
        try {
            val userId = supabaseClient.auth.currentUserOrNull()?.id
            if (userId != null) {
                getSingleById<GroupDto>(
                    table = SupabaseConstants.GROUP_TABLE,
                    id = groupId
                ) { groupDto ->
                    if (groupDto != null) {
                        supabaseClient.postgrest[SupabaseConstants.GROUP_MEMBER_TABLE]
                            .upsert(
                                GroupMemberDto(
                                    group_id = groupId,
                                    user_id = userId,
                                    role = "member"
                                )
                            )
                        emit(SupabaseOperation.Success("Group joined"))
                    } else {
                        emit(SupabaseOperation.Failure(NullPointerException("Group not available.\n" +
                                "Either you entered an invalid id or the admin deleted it.")))
                        return@flow
                    }
                }
            } else {
                emit(SupabaseOperation.Failure(NullPointerException("Account not found")))
                return@flow
            }
        } catch (e: IOException) {
            emit(SupabaseOperation.Failure(Exception("Please check internet connection")))
        }
        catch (e: Exception) {
            emit(SupabaseOperation.Failure(Exception("Please enter a valid Id.")))
        }
    }

    override fun getSpecificGroup(groupId: String): Flow<SupabaseOperation<Group>> {
        return flow {
            try {
                getSingleById<GroupDto>(
                    table = SupabaseConstants.GROUP_TABLE,
                    id = groupId
                ) { groupDto ->
                   if (groupDto != null) {
                       Log.d("Group", "getSpecificGroup: $groupDto")
                       emit(SupabaseOperation.Success(groupDto.toGroup(groupDto.admin)))
                   }
                }
            } catch (e: Exception) {
                Log.e("Group", "getSpecificGroup: $e", )
            }
        }
    }

    override suspend fun createSharedGoal(sharedGoalDto: SharedGoalDto): SupabaseOperation<String> {
        return try {
            supabaseClient.postgrest[SupabaseConstants.SHARED_GOALS_TABLE]
                .upsert(sharedGoalDto)
            SupabaseOperation.Success("Goal Created")
        } catch (e: IOException) {
            SupabaseOperation.Failure(Exception("Please check internet connection"))
        }
        catch (e: Exception) {
            Log.e("Create-Shared-Repo", "createSharedGoal: $e", )
            SupabaseOperation.Failure(Exception("Group creation failed"))
        }
    }

    override fun getSharedGoalsForGroup(groupId: String): Flow<SupabaseOperation<List<SharedGoal>>> = flow {
        try {
            getByForeignKey<SharedGoalDto>(
                table = SupabaseConstants.SHARED_GOALS_TABLE,
                column = "group_id",
                refId = groupId
            ) {sharedGoalDtos ->
                if (sharedGoalDtos.isNotEmpty()) {
                    val sharedGoals = sharedGoalDtos.map { it.toSharedGoal() }
                    Log.d("Shared-Goal", "getSharedGoalsForGroup: $sharedGoals")
                    emit(SupabaseOperation.Success(sharedGoals))
                } else {
                    Log.d("Shared-Goal", "getSharedGoalsForGroup: No goals")
                    emit(SupabaseOperation.Success(emptyList()))
                }
            }
        } catch (e: IOException) {
            emit(SupabaseOperation.Failure(Exception(IOException("Please check internet connection"))))
        } catch (e: Exception) {
            Log.e("Shared-Goal", "getSharedGoalsForGroup: ${e.localizedMessage}")
            emit(SupabaseOperation.Failure(Exception("Error fetching goals")))
        }
    }

    override fun getGroupMembersForSpecificGroup(groupId: String): Flow<List<GroupMemberDto>> {
        return flow {
            try {
                getByForeignKey<GroupMemberDto>(
                    table = SupabaseConstants.GROUP_MEMBER_TABLE,
                    column = "group_id",
                    refId = groupId
                ) {
                    Log.d("Group-Mem", "getGroupMembers: $it")
                }
            } catch (e: Exception) {
                Log.e("Group-Mem", "getSpecificGroup: $e", )
            }
        }
    }
    private suspend inline fun getGroupMembers(
        members: (List<GroupMemberDto>) -> Unit
    ) {
        val members = supabaseClient.postgrest[SupabaseConstants.GROUP_MEMBER_TABLE]
            .select()
            .decodeList<GroupMemberDto>()

        members(members)
    }

    // Maybe I will use this to get a single value from a table instead of writing the same logic again and again
    private suspend inline fun <reified T: Any> getSingleById(
        table: String,
        id: String,
        data: (T?) -> Unit
    ) {
        val result = supabaseClient.postgrest[table]
            .select {
                filter {
                    eq("id", id)
                }
            }
            .decodeSingleOrNull<T>()
        data(result)
    }

    private suspend inline fun <reified T: Any> getByForeignKey(
        table: String,
        column: String,
        refId: String,
        data: (List<T>) -> Unit
    ) {
        val result = supabaseClient.postgrest[table]
            .select {
                filter {
                    eq(column, refId)
                }
            }
            .decodeList<T>()
        data(result)
    }
}




















