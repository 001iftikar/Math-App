package com.example.mathapp.data.remote.model

data class SharedGoalDto(
    val id: String = "",
    val group_id: String = "",
    val title: String = "",
    val description: String = "",
    val is_completed: Boolean = false,
    val created_at: String = "",
    val end_by: String = "",

)