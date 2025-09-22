package com.example.mathapp.data.repository

import android.util.Log
import com.example.mathapp.data.remote.SupabaseOperation
import com.example.mathapp.data.remote.model.GroupDto
import com.example.mathapp.data.remote.model.GroupMemberDto
import com.example.mathapp.data.remote.model.MessageDto
import com.example.mathapp.data.remote.model.SharedGoalDto
import com.example.mathapp.domain.model.Group
import com.example.mathapp.domain.model.Message
import com.example.mathapp.domain.model.SharedGoal
import com.example.mathapp.domain.model.UserProfile
import com.example.mathapp.domain.repository.SharedGoalRepository
import com.example.mathapp.mappers.toGroup
import com.example.mathapp.mappers.toMessage
import com.example.mathapp.mappers.toSharedGoal
import com.example.mathapp.utils.SupabaseConstants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseError.NETWORK_ERROR
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Count
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.io.IOException
import java.util.UUID
import javax.inject.Inject

class SharedGoalRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val database: FirebaseDatabase
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
                        val userProfiles =
                            supabaseClient.postgrest[SupabaseConstants.PROFILES_TABLE]
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
                // check if a user is already in the table for the group
                val exists = (supabaseClient.postgrest[SupabaseConstants.GROUP_MEMBER_TABLE]
                    .select {
                        // no need for data, just check if there's any row exists
                        head = true
                        count(Count.EXACT)
                        filter {
                            and {
                                eq("group_id", groupId)
                                eq("user_id", userId)
                            }
                        }
                    }
                    .countOrNull() ?: 0L) > 0L
                Log.d("Group-Join", exists.toString())
                if (exists) {
                    emit(SupabaseOperation.Failure(Exception("You are already in this group")))
                    return@flow
                } else {
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
                            emit(
                                SupabaseOperation.Failure(
                                    NullPointerException(
                                        "Group not available.\n" +
                                                "Either you entered an invalid id or the admin deleted it."
                                    )
                                )
                            )
                            return@flow
                        }
                    }
                }

            } else {
                emit(SupabaseOperation.Failure(NullPointerException("Account not found")))
                return@flow
            }
        } catch (e: IOException) {
            emit(SupabaseOperation.Failure(Exception("Please check internet connection")))
        } catch (e: Exception) {
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
            } catch (e: IOException) {
                emit(SupabaseOperation.Failure(IOException("Network error")))
            } catch (e: Exception) {
                Log.e("Group", "getSpecificGroup: $e")
                emit(SupabaseOperation.Failure(e))
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
        } catch (e: Exception) {
            Log.e("Create-Shared-Repo", "createSharedGoal: $e")
            SupabaseOperation.Failure(Exception("Group creation failed"))
        }
    }

    override fun getSharedGoalsForGroup(groupId: String): Flow<SupabaseOperation<List<SharedGoal>>> =
        flow {
            try {
                getByForeignKey<SharedGoalDto>(
                    table = SupabaseConstants.SHARED_GOALS_TABLE,
                    column = "group_id",
                    refId = groupId
                ) { sharedGoalDtos ->
                    if (sharedGoalDtos.isNotEmpty()) {
                        val sharedGoals = sharedGoalDtos
                            .sortedBy({
                                it.created_at
                            })
                            .map { it.toSharedGoal() }
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

    override fun getGroupMembersForSpecificGroup(groupId: String): Flow<SupabaseOperation<List<UserProfile>>> {
        return flow {
            try {
                getByForeignKey<GroupMemberDto>(
                    table = SupabaseConstants.GROUP_MEMBER_TABLE,
                    column = "group_id",
                    refId = groupId
                ) { memberDtos ->
                    val userIds = memberDtos.map {
                        it.user_id
                    }
                    val profiles = supabaseClient.postgrest[SupabaseConstants.PROFILES_TABLE]
                        .select {
                            filter {
                                isIn("id", userIds) // no need for for-each
                            }
                        }
                        .decodeList<UserProfile>()
                    emit(SupabaseOperation.Success(profiles))
                }
            } catch (e: IOException) {
                emit(SupabaseOperation.Failure(IOException("Network error")))
            } catch (e: Exception) {
                emit(SupabaseOperation.Failure(Exception("Fetching error")))
            }
        }
    }

    override fun isGroupAdmin(groupId: String): Flow<SupabaseOperation<Boolean>> = flow {
        try {
            val userId = supabaseClient.auth.currentUserOrNull()?.id ?: return@flow emit(
                SupabaseOperation.Failure(
                    NullPointerException("User not logged in")
                )
            )
            getSingleById<GroupDto>(
                table = SupabaseConstants.GROUP_TABLE,
                id = groupId
            ) { groupDto ->
                if (groupDto != null) {
                    emit(SupabaseOperation.Success(userId == groupDto.admin))
                } else {
                    emit(SupabaseOperation.Failure(Exception("Group not found")))
                }
            }
        } catch (e: IOException) {
            emit(SupabaseOperation.Failure(IOException("No internet")))
        } catch (e: Exception) {
            Log.e("Admin-check", "isGroupAdmin: ${e.localizedMessage}")
            emit(SupabaseOperation.Failure(Exception("Please try again later")))
        }
    }

    override suspend fun sendMessage(content: String, groupId: String): SupabaseOperation<Unit> {
        // Let's use some other error handling method instead of try catching always
        return runCatching {
            val userId = supabaseClient.auth.currentUserOrNull()?.id
                ?: throw NullPointerException("User not logged in")
            val messagesRef = database.getReference("Messages")
            val child = UUID.randomUUID().toString()
            messagesRef.child(child)
                .setValue(MessageDto(
                    content = content,
                    groupId = groupId,
                    sender = userId
                ))
                .await() // used await() bcz setValue is asynchronous but runCatching is synchronous
            Unit
        }.fold(
            onSuccess = { SupabaseOperation.Success(it) },
            onFailure = { SupabaseOperation.Failure(Exception(it.message)) }
        )
    }

    override suspend fun getCurrentUserId(): String? {
        return try {
            supabaseClient.auth.currentUserOrNull()?.id
        } catch (e: Exception) {
            Log.e("User-Repo", "getCurrentUserId: ${e.localizedMessage}")
            null
        }
    }

    override suspend fun markAsCompleted(isCompleted: Boolean, sharedGoalId: String): SupabaseOperation<Boolean> {
        try {
            supabaseClient.postgrest[SupabaseConstants.SHARED_GOALS_TABLE]
                .update(
                    {
                        SharedGoalDto::is_completed setTo isCompleted
                    }
                ) {
                    filter {
                        SharedGoalDto::id eq sharedGoalId
                    }
                }

            return SupabaseOperation.Success(true)
        } catch (ex: Exception) {
            return SupabaseOperation.Failure(ex)
        }
    }

    // Unfortunately I will have to use Fb realtime db
    override fun getMessages(groupId: String): Flow<SupabaseOperation<List<Message>>> = callbackFlow {
        val messagesRef = database.getReference("Messages")
        val profiles = supabaseClient.postgrest[SupabaseConstants.PROFILES_TABLE]
            .select()
            .decodeList<UserProfile>()
        val profile = profiles.associateBy {
            it.id
        }
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = snapshot.children.mapNotNull {
                    it.getValue(MessageDto::class.java)
                }
                    .sortedByDescending { it.createdAt }
                    .filter {
                        it.groupId == groupId
                    }
                    .map {
                        val senderName = profile[it.sender]?.name ?: "Unknown"
                        it.toMessage(senderName)
                    }
                trySend(SupabaseOperation.Success(messages))
            }

            override fun onCancelled(error: DatabaseError) {
                when {
                    error.code == NETWORK_ERROR -> {
                        trySend(SupabaseOperation.Failure(Exception("Please check internet connection")))
                    }

                    else -> {
                        trySend(SupabaseOperation.Failure(Exception(
                            "Please try again: ${error.message}"
                        )))
                    }
                }
            }
        }

        messagesRef.addValueEventListener(listener)

        awaitClose {
            messagesRef.removeEventListener(listener)
        }
    }

    override suspend fun deleteGroup(groupId: String): SupabaseOperation<Unit> {
        return try {
            supabaseClient.postgrest[SupabaseConstants.GROUP_TABLE]
                .delete {
                    filter {
                        GroupDto::id eq groupId
                    }
                }
            SupabaseOperation.Success(Unit)
        } catch (e: Exception) {
            SupabaseOperation.Failure(e)
        }
    }
    private suspend inline fun getGroupMembers(
        members: (List<GroupMemberDto>) -> Unit
    ) {
        try {
            val members = supabaseClient.postgrest[SupabaseConstants.GROUP_MEMBER_TABLE]
                .select()
                .decodeList<GroupMemberDto>()

            members(members)
        } catch (e: IOException) {
            throw e
        }
        catch (e: Exception) {
            throw e
        }
    }

    // Maybe I will use this to get a single value from a table instead of writing the same logic again and again
    private suspend inline fun <reified T : Any> getSingleById(
        table: String,
        id: String,
        data: (T?) -> Unit
    ) {
        try {
            val result = supabaseClient.postgrest[table]
                .select {
                    filter {
                        eq("id", id)
                    }
                }
                .decodeSingleOrNull<T>()
            data(result)
        } catch (e: IOException) {
            throw e
        }
        catch (e: Exception) {
            throw e
        }
    }

    private suspend inline fun <reified T : Any> getByForeignKey(
        table: String,
        column: String,
        refId: String,
        data: (List<T>) -> Unit
    ) {
        try {
            val result = supabaseClient.postgrest[table]
                .select {
                    filter {
                        eq(column, refId)
                    }
                }
                .decodeList<T>()
            data(result)
        } catch (e: IOException) {
            throw e
        } catch (e: Exception) {
            throw e
        }
    }
}




















