package com.iftikar.mathapp.presentation.goal.profile_screen

import com.iftikar.mathapp.domain.model.SupabaseUser

data class ProfileScreenState(
    val isLoading: Boolean = false,
    val user: SupabaseUser = SupabaseUser(),
    val error: String? = null,
    val alertState: Boolean = false
)