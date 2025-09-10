package com.example.mathapp.presentation.goal.shared_goals.sharedgoals_screen

import com.example.mathapp.domain.model.SharedGoal

data class SharedGoalsScreenState(
    val goals: List<SharedGoal>? = null,
    val groupId: String = "",
    val groupName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)
