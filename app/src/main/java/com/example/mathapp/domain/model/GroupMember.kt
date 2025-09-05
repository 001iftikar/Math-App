package com.example.mathapp.domain.model

data class GroupMember(
    val id: String,
    val groupId: String,
    val userId: String,
    val role: String,
    val addedAt: String
)
