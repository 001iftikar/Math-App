package com.iftikar.mathapp.presentation.goal.shared_goals.sharedgoals_screen

import com.iftikar.mathapp.domain.model.SharedGoal

data class SharedGoalsScreenState(
    val goals: List<SharedGoal>? = null,
    val groupId: String = "",
    val groupName: String = "",
    val isAdmin: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)