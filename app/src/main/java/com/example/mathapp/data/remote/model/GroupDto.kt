package com.example.mathapp.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    val id: String = "",
    val admin: String = "",
    val name: String = "",
    val description: String? = null,
    val created_at: String = ""
)
