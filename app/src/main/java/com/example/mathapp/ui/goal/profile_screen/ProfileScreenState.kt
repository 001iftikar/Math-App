package com.example.mathapp.ui.goal.profile_screen

import com.example.mathapp.domain.model.SupabaseUser

data class ProfileScreenState(
    val isLoading: Boolean = false,
    val user: SupabaseUser = SupabaseUser(),
    val error: String? = null,
    val completedTasks: Int = 0,
    val ongoingTasks: Int = 0
)