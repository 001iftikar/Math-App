package com.example.mathapp.presentation.goal.shared_goals.creategroup_screen

data class CreateGroupScreenState(
    val title: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
