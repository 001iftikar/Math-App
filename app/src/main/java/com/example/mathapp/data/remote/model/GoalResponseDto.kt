package com.example.mathapp.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoalResponseDto(
    val id: String,
    @SerialName("created_at")
    val createdAt: String,
    val title: String,
    val description: String,
    @SerialName("end_by")
    val endBy: String,
    @SerialName("is_completed")
    val isCompleted: Boolean,
    @SerialName("user_id")
    val userId: String
)
