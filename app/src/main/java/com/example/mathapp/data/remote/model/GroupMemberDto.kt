package com.example.mathapp.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class GroupMemberDto(
    val id: String = "",
    val group_id: String = "",
    val user_id: String = "",
    val role: String = "",
    val added_at: String = ""
)
