package com.iftikar.mathapp.presentation.goal.shared_goals.groups_screen

import com.iftikar.mathapp.domain.model.Group

data class GroupsScreenState(
    val groups: List<Group>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
