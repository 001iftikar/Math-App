package com.example.mathapp.domain.model

data class SupabaseUser(
    val userId: String = "",
    val email: String = "",
    val name: String = "",
    val completedTasks: String = "",
    val ongoingTasks: String = ""
)
