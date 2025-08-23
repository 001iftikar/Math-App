package com.example.mathapp.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoalRequestDto(
    val id: String? = null,
    @SerialName("user_id")
    val userId: String = "", // foreign key to get the reference of the user of the goal
    val title: String = "",
    val description: String = "",
    @SerialName("end_by")
    val endBy: String
)
